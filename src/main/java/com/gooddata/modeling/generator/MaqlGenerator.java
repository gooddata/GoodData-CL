package com.gooddata.modeling.generator;

import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * GoodData MAQL Generator generates the MAQL from the LDM schema object
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class MaqlGenerator {

    /**
     * Generates the MAQL from the schema
     * @param schema the LDM schema object
     * @return the MAQL as a String
     */
    public String generateMaql(SourceSchema schema) {
        String script = "";

        String ssn = StringUtil.formatShortName(schema.getName());
        String lsn = StringUtil.formatLongName(schema.getName());
        script += "CREATE DATASET {dataset." + ssn + "} VISUAL(TITLE \"" + lsn + "\");\n\n";

        // Populate the attribute and the fact folders
        ArrayList<String> attributeFolders = new ArrayList<String>();
        ArrayList<String> factFolders = new ArrayList<String>();
        for (SourceColumn column : schema.getColumns()) {
            String folder = column.getFolder();
            if (folder != null && folder.length() > 0) {
                if (column.getLdmType().equalsIgnoreCase(SourceColumn.LDM_TYPE_ATTRIBUTE) ||
                        column.getLdmType().equalsIgnoreCase(SourceColumn.LDM_TYPE_LABEL)) {
                    if (!attributeFolders.contains(folder))
                        attributeFolders.add(folder);
                }
                if (column.getLdmType().equalsIgnoreCase(SourceColumn.LDM_TYPE_FACT)) {
                    if (!factFolders.contains(folder))
                        factFolders.add(folder);
                }
            }
        }

        // Generate statements for the ATTRIBUTE folders
        for (String folder : attributeFolders) {
            String sfn = StringUtil.formatShortName(folder);
            String lfn = StringUtil.formatLongName(folder);
            script += "CREATE FOLDER {dim." + sfn + "} VISUAL(TITLE \"" + lfn + "\") TYPE ATTRIBUTE;\n";
        }

        script += "\n";

        // Generate statements for the FACT folders
        for (String folder : factFolders) {
            String sfn = StringUtil.formatShortName(folder);
            String lfn = StringUtil.formatLongName(folder);
            script += "CREATE FOLDER {ffld." + sfn + "} VISUAL(TITLE \"" + lfn + "\") TYPE FACT;\n";
        }

        script += "\n";

        boolean hasFacts = false;
        // we need to push all labels to the very bottom
        // as these might reference attributes that hasn't been generated yet
        List<SourceColumn> labels = new ArrayList<SourceColumn>();

        // generate attributes and facts
        for (SourceColumn column : schema.getColumns()) {
            String scn = StringUtil.formatShortName(column.getName());
            String lcn = StringUtil.formatLongName(column.getTitle());
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_ATTRIBUTE)) {
                String folderStatement = "";
                String folder = column.getFolder();
                if (folder != null && folder.length() > 0) {
                    String sfn = StringUtil.formatShortName(folder);
                    folderStatement = ", FOLDER {dim." + sfn + "}";
                }
                script += "CREATE ATTRIBUTE {attr." + ssn + "." + scn + "} VISUAL(TITLE \"" + lcn
                        + "\"" + folderStatement + ") AS KEYS {d_" + ssn + "_" + scn + ".id} FULLSET, {f_" + ssn + "."
                        + scn + "_id} WITH LABELS {label." + ssn + "." + scn + "} VISUAL(TITLE \""
                        + lcn + "\") AS {d_" + ssn + "_" + scn + ".nm_" + scn + "};\n";
                script += "ALTER DATASET {dataset." + ssn + "} ADD {attr." + ssn + "." + scn + "};\n\n";
            }
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_FACT)) {
                hasFacts = true;
                String folderStatement = "";
                String folder = column.getFolder();
                if (folder != null && folder.length() > 0) {
                    String sfn = StringUtil.formatShortName(folder);
                    folderStatement = ", FOLDER {ffld." + sfn + "}";
                }
                script += "CREATE FACT {fact." + ssn + "." + scn + "} VISUAL(TITLE \"" + lcn
                        + "\"" + folderStatement + ") AS {f_" + ssn + ".f_" + scn + "};\n";
                script += "ALTER DATASET {dataset." + ssn + "} ADD {fact." + ssn + "." + scn + "};\n\n";
            }
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_DATE)) {
                hasFacts = true;
                String folderStatement = "";
                String folder = column.getFolder();
                if (folder != null && folder.length() > 0) {
                    String sfn = StringUtil.formatShortName(folder);
                    folderStatement = ", FOLDER {ffld." + sfn + "}";
                }
                script += "CREATE FACT {dt." + ssn + "." + scn + "} VISUAL(TITLE \"" + lcn
                        + "\"" + folderStatement + ") AS {f_" + ssn + ".dt_" + scn + "};\n";
                script += "ALTER DATASET {dataset." + ssn + "} ADD {dt." + ssn + "." + scn + "};\n\n";
            }
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_LABEL)) {
                labels.add(column);
            }

        }

        // alter the attributes with labels. This needs to happen after all attributes are defined as these might
        // be referenced
        for (SourceColumn column : labels) {
            String scn = StringUtil.formatShortName(column.getName());
            String lcn = StringUtil.formatLongName(column.getTitle());
            String scnPk = StringUtil.formatShortName(column.getPk());
            script += "ALTER ATTRIBUTE {attr." + ssn + "." + scnPk + "} ADD LABELS {label." + ssn + "." + scnPk + "."
                    + scn + "} VISUAL(TITLE \"" + lcn + "\") AS {d_" + ssn + "_" + scnPk + ".nm_" + scn + "};\n\n";
        }

        // generate the facts of / record id special attribute
        if (hasFacts) {
            script += "CREATE ATTRIBUTE {attr." + ssn + ".factsof" + "} VISUAL(TITLE \"" + lsn +
                    " Record ID\") AS KEYS {f_" + ssn + ".id} FULLSET;\n";
            script += "ALTER DATASET {dataset." + ssn + "} ADD {attr." + ssn + ".factsof};\n\n";
        }

        // finally synchronize
        script += "SYNCHRONIZE {dataset." + ssn + "};";
        return script;
    }


}
