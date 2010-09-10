/*
 * Copyright (c) 2009, GoodData Corporation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of conditions and
 *        the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *        and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *     * Neither the name of the GoodData Corporation nor the names of its contributors may be used to endorse
 *        or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.snaplogic.snap.gooddata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.snaplogic.cc.Capabilities;
import org.snaplogic.cc.Capability;
import org.snaplogic.cc.InputView;
import org.snaplogic.cc.OutputView;
import org.snaplogic.cc.prop.SimpleProp;
import org.snaplogic.cc.prop.SimpleProp.SimplePropType;
import org.snaplogic.common.ComponentResourceErr;
import org.snaplogic.common.Field;
import org.snaplogic.common.Record;
import org.snaplogic.common.Field.SnapFieldType;
import org.snaplogic.common.exceptions.SnapComponentException;
import org.snaplogic.log.Log;
import org.snaplogic.snapi.PropertyConstraint;
import org.snaplogic.snapi.PropertyConstraint.Type;
import org.snaplogic.util.ConvertUtils;

import com.gooddata.exception.GdcProjectAccessException;
import com.gooddata.exception.GdcRestApiException;
import com.gooddata.exception.GdcUploadErrorException;
import com.gooddata.exception.HttpMethodException;
import com.gooddata.integration.ftp.GdcFTPApiWrapper;
import com.gooddata.integration.model.Project;
import com.gooddata.integration.rest.GdcRESTApiWrapper;
import com.gooddata.util.FileUtil;

public class GoodDataPut extends AbstractGoodDataComponent {

	@Override
	public String getDescription() {
		return getLabel() + ". Uploads data into GoodData.";
	}

	@Override
	public Capabilities getCapabilities() {
		return new Capabilities() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3848994857590856035L;

			{
				put(Capability.INPUT_VIEW_LOWER_LIMIT, 1);
				put(Capability.INPUT_VIEW_UPPER_LIMIT, 100);
				put(Capability.OUTPUT_VIEW_LOWER_LIMIT, 0);
				put(Capability.OUTPUT_VIEW_UPPER_LIMIT, 0);
			}
		};
	}

	@Override
	public String getLabel() {
		return "GoodData Upload (Java)";
	}

	private static final String DLI_MANIFEST_FILENAME = "upload_info.json";
	private static final String DLI_ARCHIVE_SUFFIX = ".zip";

	public static final String PROP_PROJECT_NAME = "project_name";

	public static final String PROP_DLI = "dli";

	public static final String PROP_OVERWRITE = "overwrite";

	@Override
	public String getAPIVersion() {
		return "1.0";
	}

	@Override
	public String getComponentVersion() {
		return "1.0";
	}

	private PropertyConstraint projNameLovConstraint = new PropertyConstraint();

	private SimpleProp projNameProp = new SimpleProp("Project name", SimplePropType.SnapString, "Project name",
			projNameLovConstraint, true);

	private PropertyConstraint dlisLovConstraint = new PropertyConstraint();

	private SimpleProp dliProp = new SimpleProp("SLI", SimplePropType.SnapString, "Data Loading Interface",
			dlisLovConstraint, true);

	@Override
	public void createResourceTemplate() {
		super.createResourceTemplate();
		setPropertyDef(PROP_PROJECT_NAME, projNameProp);
		setPropertyDef(PROP_DLI, dliProp);
		setPropertyDef(PROP_OVERWRITE, new SimpleProp("Overwrite", SimplePropType.SnapBoolean,
				"Overwrite if true, update if false", true));
		setPropertyValue(PROP_OVERWRITE, true);
	}

	@Override
	public void validate(ComponentResourceErr err) {
		GdcRESTApiWrapper restApi = login(err);
		if (restApi == null) {
			return;
		}
		String curProjName = (String) getPropertyValue(PROP_PROJECT_NAME);
		try {
			if (curProjName != null && restApi.getProjectByName(curProjName) == null) {
				err
						.getPropertyErr(PROP_PROJECT_NAME)
						.setMessage(
								"Project '"
										+ curProjName
										+ "' does not exist for this account. Use auto-fill to populate this property with a list of eligible projects.");
			}
		} catch (HttpMethodException hme) {
			elog(hme);
			err.setMessage("Error occurred validating project name: " + hme.getMessage());
		}
		// TODO check that view matches the metadata
		catch (GdcProjectAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void suggestResourceValues(ComponentResourceErr err) {
		info("Auto-Fill for GoodDataSnap started...");
		super.suggestResourceValues(err);
		GdcRESTApiWrapper restApi = login(err);
		if (restApi == null) {
			return;
		}

		info("Logged in GooData. Listing project...");
		HashMap<String, Project> projNames;
		try {
			projNames = getProjectList(restApi);
		} catch (HttpMethodException hme) {
			elog(hme);
			err.setMessage("Unable to get list of projects from Good Data: " + hme.getMessage());
			return;
		}

		if (projNames.size() == 0) {
			err.getPropertyErr(PROP_PROJECT_NAME).setMessage("No projects exist for this account");
			return;
		}

		projNameLovConstraint.clear();
		projNameLovConstraint.put(Type.LOV, projNames.keySet().toArray());
		String curProjName = (String) getPropertyValue(PROP_PROJECT_NAME);
		getResdef().removePropertyDef(PROP_PROJECT_NAME);
		setPropertyDef(PROP_PROJECT_NAME, projNameProp);
		if (curProjName != null && curProjName.trim().length() == 0) {
			// Wasn't set, so we suggest first one in the list
			curProjName = (String) projNames.keySet().toArray()[0];
		} else if (!projNames.keySet().contains(curProjName)) {
			// Was set but not in current list
			curProjName = (String) projNames.keySet().toArray()[0];
		}
		setPropertyValue(PROP_PROJECT_NAME, curProjName);
		info("Current project is: " + curProjName);

		List<String> dlisForCurProject = new ArrayList<String>();
		Project proj = projNames.get(curProjName);
		//List<DLI> interfaces;

		try {
			//interfaces = restApi.getDLIs(proj.getId());
		} catch (HttpMethodException e) {
			elog(e);
			err.setMessage("Unable to update project with DLIs: " + String.format(e.getMessage(), proj.getName(), " "));
			return;
		} catch (GdcProjectAccessException e) {
			elog(e);
			err.setMessage("Unable to update project with DLIs: " + String.format(e.getMessage(), proj.getName(), " "));
			return;
		}

		info("For project name: " + proj.getName());
		//for (DLI dli : interfaces) {
		//	info("   Adding SLI: " + dli.getName());
		//	dlisForCurProject.add(dli.getName());
		//}

		//dlisLovConstraint.clear();
		dlisLovConstraint.put(Type.LOV, dlisForCurProject.toArray());
		String curDli = (String) getPropertyValue(PROP_DLI);
		getResdef().removePropertyDef(PROP_DLI);
		setPropertyDef(PROP_DLI, dliProp);
		if (curDli != null && curDli.trim().length() == 0) {
			// Wasn't set, so we suggest first one in the list
			curDli = dlisForCurProject.get(0);
		} else if (!dlisForCurProject.contains(curDli)) {
			// Was set but not in current list
			curDli = dlisForCurProject.get(0);

		}
		setPropertyValue(PROP_DLI, curDli);
		info("Current SLI is:" + curDli);

		// remove existing input views
		String[] inViewNames = listInputViewNames().toArray(new String[0]);

		info("Removing old views...");
		for (String inViewName : inViewNames) {
			removeInputViewDef(inViewName);
			info("  View " + inViewName + " removed.");
		}

		//List<DLIPart> dliParts = null;
		try {
			//DLI dli = restApi.getDLIByName(curDli, proj.getId());
			//dliParts = restApi.getDLIParts(dli.getId(), proj.getId());
		} catch (HttpMethodException e) {
			info("Unable to update project with SLI Parts: " + String.format(e.getMessage(), curDli, " "));
		} catch (GdcProjectAccessException e) {
			info("Unable to update project with SLI Parts: " + String.format(e.getMessage(), curDli, " "));
		}

		//if (dliParts != null) {
		//	for (DLIPart part : dliParts) {
				// Finally, set the view...
				//String inViewName = part.getFileName().replaceAll(".csv", "");
				List<Field> fields = new ArrayList<Field>();

				//List<Column> columns = part.getColumns();
				//for (Column column : columns) {
				//	String fieldName = column.getName();

					Field f;
				//	if (column.getType().toLowerCase().startsWith("varchar")) {
				//		f = new Field(fieldName, SnapFieldType.SnapString);
				//	} else if (column.getType().toLowerCase().startsWith("date")) {
				//		f = new Field(fieldName, SnapFieldType.SnapDateTime);
				//	} else {
				//		f = new Field(fieldName, SnapFieldType.SnapNumber);
				//	}

				//	fields.add(f);
				//}

				//info("Storing new input view: " + inViewName);
				//addRecordInputViewDef(inViewName, fields, "Input view for project " + curProjName + ", SLI " + curDli,
				//		true);
			//}
		//} else {
		//	info("No input view created, since there're no parts for selected Project & SLI");
		//}
	}

	private HashMap<String, Project> getProjectList(GdcRESTApiWrapper restApi) throws HttpMethodException {
		List<Project> projects = restApi.listProjects();

		HashMap<String, Project> projNames = new HashMap<String, Project>();
		for (Project proj : projects) {
			info("  Adding project to the list of available projects: " + proj.getName());
			projNames.put(GoodDataApiHelper.getProjectLabel(proj), proj);
		}
		return projNames;
	}

	// TODO
	// this pattern should also be used in other places where
	// writes to intermediate file or db are done
	// even better to keep them in a StringBuffer-like object, not a list
	// (so writes are in one swoop) but StringBuffer doesn't seem to have
	// a clear() method, or maybe it's just late in the day

	protected final int getRecordBufSize() {
		if (Log.isLevelDebug()) {
			return 16;
		} else {
			return 1024 * 10;
		}
	}

	protected Map<String, File> stageInputsAsCsv(Map<String, InputView> inputs, boolean createHeader,
			boolean useFieldDescForHeader, File tmpDir) {
		List<InputView> activeInputViews = new ArrayList<InputView>();
		activeInputViews.addAll(inputs.values());
		if (activeInputViews.size() == 0) {
			throw new SnapComponentException("Not enough input views available");
		}
		Map<String, File> viewToCsvFile = new HashMap<String, File>();
		Map<String, BufferedWriter> viewToCsvWriter = new HashMap<String, BufferedWriter>();

		// TODO
		Map<String, List<String>> bufferedRecords = new HashMap<String, List<String>>();
		try {
			while (activeInputViews.size() > 0) {
				Collection<InputView> viewsWithData = selectInputView(activeInputViews);
				Collection<InputView> finishedViewList = new ArrayList<InputView>();
				// Get the records from all the views that currently have data
				for (InputView view : viewsWithData) {
					String viewName = view.getName();
					File csvFile = viewToCsvFile.get(viewName);
					// csvFile.deleteOnExit();
					BufferedWriter csvWriter = viewToCsvWriter.get(viewName);
					if (csvFile == null) {

						csvFile = new File(tmpDir, viewName + ".csv");
						info("Creating stage CSV file: " + csvFile.getAbsolutePath());
						csvWriter = FileUtil.createBufferedUtf8Writer(csvFile);
						viewToCsvFile.put(viewName, csvFile);
						viewToCsvWriter.put(viewName, csvWriter);
					}
					Record record = view.readRecord();
					if (record == null) {
						// We get null as a record, if the view is closing on
						// the other side. We are keeping track of those that
						// have
						// closed in this round, and create a reverse sorted
						// list of their indices in the active_input_view list.
						// We can't just simply erase them right now out of the
						// list since that would screw up our iteration over the
						// list.
						finishedViewList.add(view);
						continue;
					}
					List<String> curBuffer = bufferedRecords.get(viewName);
					if (curBuffer == null) {
						curBuffer = new ArrayList<String>(getRecordBufSize());
						bufferedRecords.put(viewName, curBuffer);
						if (createHeader) {
							// No buffer yet created, so write a header
							String header = "";
							boolean first = true;
							for (Field field : view.getFields()) {
								if (first) {
									first = false;
								} else {
									header += ",";
								}
								if (useFieldDescForHeader) {
									header += "\"" + field.getDescription() + "\"";
								} else {
									header += "\"" + field.getName() + "\"";
								}

							}
							curBuffer.add(header);
						}

					}
					String curLine = "";
					boolean first = true;
					for (Field field : view.getFields()) {
						Object val = record.get(field.getName());
						String stringVal = null;
						if (val == null) {
							stringVal = "";
						} else if (val instanceof String) {
							stringVal = val.toString();
							stringVal = stringVal.replace('"', '\"');
						} else if (val instanceof BigDecimal) {
							stringVal = val.toString();
						} else if (val instanceof Timestamp) {
							stringVal = ISO_DATE_FORMAT.format((Timestamp) val);
						}
						if (first) {
							first = false;
						} else {
							curLine += ",";
						}
						curLine += "\"" + stringVal + "\"";
					}
					curBuffer.add(curLine);

					if (curBuffer.size() > getRecordBufSize()) {
						debug("Writing " + curBuffer.size() + " lines to " + csvFile);
						for (String line : curBuffer) {
							csvWriter.write(line);
							csvWriter.write(System.getProperty("line.separator"));
						}
						csvWriter.flush();
						curBuffer.clear();
					}

				}
				// Any views that have finished in the last round?
				for (InputView view : finishedViewList) {
					// Now we can delete them (starting from the back) from the
					// list of active views.
					activeInputViews.remove(view);
				}
			}

			// Write out the buffers
			for (String viewName : bufferedRecords.keySet()) {
				List<String> curBuffer = bufferedRecords.get(viewName);
				File csvFile = viewToCsvFile.get(viewName);
				BufferedWriter csvWriter = viewToCsvWriter.get(viewName);
				if (curBuffer.size() > 0) {
					debug("Writing " + curBuffer.size() + " lines to " + csvFile);
					for (String line : curBuffer) {
						csvWriter.write(line);
						csvWriter.write(System.getProperty("line.separator"));
					}
					csvWriter.flush();
					csvWriter.close();
					curBuffer.clear();
				}

			}
			return viewToCsvFile;
		} catch (IOException ioe) {
			// TODO be more graceful here
			elog(ioe);
			throw new SnapComponentException(ioe);
		}
	}

	public static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat(ConvertUtils.DATE_FORMAT_1);

	@Override
	public void execute(Map<String, InputView> inputViews, Map<String, OutputView> outputViews) {

		info("Staged CSVs will be uploaded to GoodData. Logging in...");
		GdcRESTApiWrapper restApi = login();
		GdcFTPApiWrapper ftpApi = ftpLogin();

		String projectLabel = (String) getPropertyValue(PROP_PROJECT_NAME);
		String dliName = (String) getPropertyValue(PROP_DLI);
		Boolean overwrite = (Boolean) getPropertyValue(PROP_OVERWRITE);

		if (projectLabel == null || dliName == null)
			throw new SnapComponentException("Project or SLI is not specified!");

		info("Listing projects...");
		HashMap<String, Project> projectList;
		try {
			projectList = getProjectList(restApi);
		} catch (HttpMethodException hme) {
			elog(hme);
			throw new SnapComponentException(hme);
		}

		String projectId = projectList.get(projectLabel).getId();
		info("Integration starting for project ID " + projectId);

		Map<String, File> stagedCSVs = null;
		String archivePath = null;

		try {
			Project project = restApi.getProjectById(projectId);
			//DLI dli = restApi.getDLIByName(dliName, projectId);
			//List<DLIPart> parts = restApi.getDLIParts(dli.getId(), projectId);

			if (overwrite != null) {
				//for (DLIPart part : parts) {
				//	if (overwrite.booleanValue())
				//		part.setLoadMode(DLIPart.LM_FULL);
				//	else
				//		part.setLoadMode(DLIPart.LM_INCREMENTAL);
				//}
			}

			File tmpDir = FileUtil.createTempDir();
			File tmpZipDir = FileUtil.createTempDir();
			String archiveName = tmpDir.getName() + DLI_ARCHIVE_SUFFIX;
			archivePath = tmpZipDir.getAbsolutePath() + System.getProperty("file.separator") + archiveName;

			stagedCSVs = stageInputsAsCsv(inputViews, true, false, tmpDir);
			//FileUtil.writeStringToFile(dli.getDLIManifest(parts), tmpDir + System.getProperty("file.separator")
			//		+ DLI_MANIFEST_FILENAME);

			info("Packing and uploading contect of: " + tmpDir.getAbsolutePath());
			FileUtil.compressDir(tmpDir.getAbsolutePath(), archivePath);
			ftpApi.transferDir(archivePath);
			info("Starting integration of directory: " + tmpDir.getName());
			restApi.startLoading(project.getId(), tmpDir.getName());
			info("Integration started, check the FTP directory...");

		} catch (HttpMethodException e) {
			elog(e);
			throw new SnapComponentException(e);
		} catch (GdcProjectAccessException e) {
			elog(e);
			throw new SnapComponentException(e);
		} catch (IOException e) {
			elog(e);
			throw new SnapComponentException(e);
		} catch (GdcUploadErrorException e) {
			elog(e);
			throw new SnapComponentException(e);
		} catch (GdcRestApiException e) {
			elog(e);
			throw new SnapComponentException(e);
		} finally {
			// cleanup
			info("Cleaning-up staging CSV files and uploaded archive...");
			if (stagedCSVs != null) {
				for (Entry<String, File> file : stagedCSVs.entrySet()) {
					file.getValue().delete();
				}
			}

			if (archivePath != null) {
				new File(archivePath).delete();
			}
			info("Clean-up done");
		}
	}
}
