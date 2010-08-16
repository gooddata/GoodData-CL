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

package com.gooddata.modeling.generator;

import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.naming.N;
import com.gooddata.util.StringUtil;
import com.sun.corba.se.spi.ior.Identifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GoodData MAQL Generator generates the MAQL from the LDM schema object
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class MaqlGenerator {

    private final SourceSchema schema;
    private final String ssn, lsn;
	private final String factsOfAttrMaqlDdl;

    private Map<String, Attribute> attributes = new HashMap<String, Attribute>();
    private List<Fact> facts = new ArrayList<Fact>();
    private List<Label> labels = new ArrayList<Label>();
    private List<DateColumn> dates = new ArrayList<DateColumn>();
    private List<Reference> references = new ArrayList<Reference>();
    private boolean hasCp = false;
    private String factsOfPrimaryColumn = N.ID;

    public MaqlGenerator(SourceSchema schema) {
        this.schema = schema;
        this.ssn = StringUtil.toIdentifier(schema.getName());
        this.lsn = StringUtil.toTitle(schema.getName());
        this.factsOfAttrMaqlDdl = createFactOfMaqlDdl(schema.getName());
    }

	/**
     * Generates the MAQL from the schema
     *
     * @return the MAQL as a String
     */
    public String generateMaqlCreate() {
        String script = "# THIS IS MAQL SCRIPT THAT GENERATES PROJECT LOGICAL MODEL.\n# SEE THE MAQL DOCUMENTATION " +
                "AT http://developer.gooddata.com/api/maql-ddl.html FOR MORE DETAILS\n\n";
        script += "# CREATE DATASET. DATASET GROUPS ALL FOLLOWING LOGICAL MODEL ELEMENTS TOGETHER.\n";
        script += "CREATE DATASET {" + schema.getDatasetName() + "} VISUAL(TITLE \"" + lsn + "\");\n\n";
        script += generateFoldersMaqlDdl(schema.getColumns());
        
        script += generateMaqlAdd(schema.getColumns(), true);
        
        return script;
    }

    /**
     * Generate MAQL for specified (new) columns
     * @param columns list of columns
     * @return MAQL as String
     */
    public String generateMaqlAdd(List<SourceColumn> columns) {
    	return generateMaqlAdd(columns, false);
    }

    /**
     * Creates attribute table name
     * @param schema source schema
     * @param sc source column
     * @return the attribute table name
     */
	public static String createAttributeTableName(SourceSchema schema, SourceColumn sc) {
		final String ssn = StringUtil.toIdentifier(schema.getName());
		return "d_" + ssn + "_" + StringUtil.toIdentifier(sc.getName());
	}
	
	/**
     * Generate MAQL DROP statement for selected columns
     * @param columns list of columns
     * @param createFactsOf create the facts of attribute
     * @return MAQL as String
     */
    public String generateMaqlDrop(List<SourceColumn> columns) {
        // generate attributes and facts
        for (SourceColumn column : columns) {
            processColumn(column);
        }
        
        StringBuffer script = new StringBuffer("# DROP ATTRIBUTES.\n");
        
        for (final Column c : attributes.values()) {
            script.append(c.generateMaqlDdlDrop());
        }
        script.append("# DROP FACTS\n");
        for (final Column c : facts) {
        	script.append(c.generateMaqlDdlDrop());
        }
        script.append("# DROP DATEs\n# DATES ARE REPRESENTED AS FACTS\n");
        script.append("# DATES ARE ALSO CONNECTED TO THE DATE DIMENSIONS\n");
        for (final Column c : dates) {
            script.append(c.generateMaqlDdlDrop());
        }
        script.append("# CREATE REFERENCES\n# REFERENCES CONNECT THE DATASET TO OTHER DATASETS\n");
        for (final Column c : references) {
        	script.append(c.generateMaqlDdlDrop());
        }
        return script.toString();
    }

    /**
     * Generate MAQL for selected (new) columns
     * @param columns list of columns
     * @param createFactsOf create the facts of attribute
     * @return MAQL as String
     */
    private String generateMaqlAdd(List<SourceColumn> columns, boolean createFactsOf) {

        // generate attributes and facts
        for (SourceColumn column : columns) {
            processColumn(column);
        }

        String script = "# CREATE ATTRIBUTES.\n# ATTRIBUTES ARE CATEGORIES THAT ARE USED FOR SLICING AND DICING THE " +
                    "NUMBERS (FACTS)\n";
        for (final Column c : attributes.values()) {
            script += c.generateMaqlDdlAdd();
        }
        script += "# CREATE FACTS\n# FACTS ARE NUMBERS THAT ARE AGGREGATED BY ATTRIBUTES.\n";
        for (final Column c : facts) {
            script += c.generateMaqlDdlAdd();
        }
        script += "# CREATE DATE FACTS\n# DATES ARE REPRESENTED AS FACTS\n# DATES ARE ALSO CONNECTED TO THE " +
                "DATE DIMENSIONS\n";
        for (final Column c : dates) {
            script += c.generateMaqlDdlAdd();
        }
        script += "# CREATE REFERENCES\n# REFERENCES CONNECT THE DATASET TO OTHER DATASETS\n";
        for (final Column c : references) {
        	script += c.generateMaqlDdlAdd();
        }

        if (createFactsOf) {
	        script += "# THE FACTS OF ATTRIBUTE IS SORT OF DATASET IDENTIFIER\n";
	        script += "# IT IS USED FOR COUNT AGGREGATIONS\n";
	        // generate the facts of / record id special attribute
	        script += "CREATE ATTRIBUTE " + factsOfAttrMaqlDdl + " VISUAL(TITLE \""
	                  + "Records of " + lsn + "\") AS KEYS {" + getFactTableName() + "."+factsOfPrimaryColumn+"} FULLSET;\n";
	        script += "ALTER DATASET {" + schema.getDatasetName() + "} ADD {attr." + ssn + ".factsof};\n\n";
        }

        // labels last
        for (final Column c : labels) {
            script += c.generateMaqlDdlAdd();
        }
        
        // finally synchronize
        script += "# SYNCHRONIZE THE STORAGE AND DATA LOADING INTERFACES WITH THE NEW LOGICAL MODEL\n";
        script += "SYNCHRONIZE {" + schema.getDatasetName() + "};";
        return script;
    }

    /**
     * Generate MAQL folders for specified columns
     * @param columns list of columns
     * @return MAQL as String
     */
    private String generateFoldersMaqlDdl(List<SourceColumn> columns) {
        final ArrayList<String> attributeFolders = new ArrayList<String>();
        final ArrayList<String> factFolders = new ArrayList<String>();

        for (SourceColumn column : columns) {
            String folder = column.getFolder();
            if (folder != null && folder.length() > 0) {
                if (column.getLdmType().equalsIgnoreCase(SourceColumn.LDM_TYPE_ATTRIBUTE) ||
                        column.getLdmType().equalsIgnoreCase(SourceColumn.LDM_TYPE_LABEL) ||
                        column.getLdmType().equalsIgnoreCase(SourceColumn.LDM_TYPE_CONNECTION_POINT) ||
                        column.getLdmType().equalsIgnoreCase(SourceColumn.LDM_TYPE_REFERENCE) ||
                        column.getLdmType().equalsIgnoreCase(SourceColumn.LDM_TYPE_DATE))
                {
                    if (!attributeFolders.contains(folder))
                        attributeFolders.add(folder);
                }
                if (column.getLdmType().equalsIgnoreCase(SourceColumn.LDM_TYPE_FACT) ||
                		column.getLdmType().equalsIgnoreCase(SourceColumn.LDM_TYPE_DATE))
                {
                    if (!factFolders.contains(folder))
                        factFolders.add(folder);
                }
            }
        }

        String script = "";
        if(!attributeFolders.isEmpty() || !factFolders.isEmpty())
            script += "# CREATE THE FOLDERS THAT GROUP ATTRIBUTES AND FACTS\n";
        // Generate statements for the ATTRIBUTE folders
        for (String folder : attributeFolders) {
            String sfn = StringUtil.toIdentifier(folder);
            String lfn = StringUtil.toTitle(folder);
            script += "CREATE FOLDER {dim." + sfn + "} VISUAL(TITLE \"" + lfn + "\") TYPE ATTRIBUTE;\n";
        }
        script += "\n";

        // Generate statements for the FACT folders
        for (String folder : factFolders) {
            String sfn = StringUtil.toIdentifier(folder);
            String lfn = StringUtil.toTitle(folder);
            script += "CREATE FOLDER {ffld." + sfn + "} VISUAL(TITLE \"" + lfn + "\") TYPE FACT;\n";
        }

        script += "\n";
        return script;
    }

    /**
     * Main loop. Process all columns in the schema
     * @param column source columns
     */
    private void processColumn(SourceColumn column) {
        if (column.getLdmType().equals(SourceColumn.LDM_TYPE_ATTRIBUTE)) {
        	Attribute attr = new Attribute(column);
            attributes.put(attr.scn, attr);
        } else if (column.getLdmType().equals(SourceColumn.LDM_TYPE_FACT)) {
            facts.add(new Fact(column));
        } else if (column.getLdmType().equals(SourceColumn.LDM_TYPE_DATE)) {
            dates.add(new DateColumn(column));
        } else if (column.getLdmType().equals(SourceColumn.LDM_TYPE_LABEL)) {
            labels.add(new Label(column));
        } else if (column.getLdmType().equals(SourceColumn.LDM_TYPE_REFERENCE)) {
        	references.add(new Reference(column));
        } else if (column.getLdmType().equals(SourceColumn.LDM_TYPE_IGNORE)) {
        	; // intentionally do nothing
        } else if (column.getLdmType().equals(SourceColumn.LDM_TYPE_CONNECTION_POINT)) {
            processConnectionPoint(column);
        } else {
        	throw new IllegalArgumentException("Unsupported ldm type '" + column.getLdmType() + "'.");
        }
    }

    /**
     * Processes connection point column
     * @param column source column
     */
    private void processConnectionPoint(SourceColumn column) {
        if (column.getLdmType().equals(SourceColumn.LDM_TYPE_CONNECTION_POINT)) {
            if (hasCp) {
                throw new IllegalStateException("Only one connection point per dataset is allowed. "
                        + "Consider declaring the duplicate connection points as labels of the main connection point.");
            }
            ConnectionPoint connectionPoint = new ConnectionPoint(column);
            attributes.put(connectionPoint.scn, connectionPoint);
        }
    }

    /**
     * Generate fact table name
     * @return fact table name
     */
    private String getFactTableName() {
    	return N.FCT_PFX + ssn;
    }

    /**
     * Generate the MAQL for the facts of attribute
     * @param schemaName schema name
     * @return facts of attribute MAQL DDL
     */
    private static String createFactOfMaqlDdl(String schemaName) {
    	return "{attr." + StringUtil.toIdentifier(schemaName) + "." + N.FACTS_OF + "}";
	}
    
    // columns

    private abstract class Column {
        protected final SourceColumn column;
        protected final String scn, lcn;
        protected final String identifier;

        Column(SourceColumn column, String idprefix) {
            this.column = column;
            this.scn = StringUtil.toIdentifier(column.getName());
            this.lcn = StringUtil.toTitle(column.getTitle());
            this.identifier = idprefix + "." + ssn + "." + scn;
        }

        protected String createForeignKeyMaqlDdl() {
           	return "{" + getFactTableName() + "." + scn + "_" + N.ID+ "}";
        }
                
        public abstract String generateMaqlDdlAdd();
        
        public String generateMaqlDdlDrop() {
        	return "DROP {" + identifier + "} CASCADE;";
        }

    }


    // attributes
    private class Attribute extends Column {

        protected final String table;

        Attribute(SourceColumn column, String table) {
            super(column, "attr");
            this.table = (table == null) ? createAttributeTableName(schema, column) : table;
        }

        Attribute(SourceColumn column) {
            this(column, null);
        }

        public String generateMaqlDdlAdd() {
            String folderStatement = "";
            String folder = column.getFolder();
            if (folder != null && folder.length() > 0) {
                String sfn = StringUtil.toIdentifier(folder);
                folderStatement = ", FOLDER {dim." + sfn + "}";
            }

            String script = "CREATE ATTRIBUTE {" + identifier + "} VISUAL(TITLE \"" + lcn
                    + "\"" + folderStatement + ") AS KEYS {" + table + "."+N.ID+"} FULLSET, ";
            script += createForeignKeyMaqlDdl();
            script += " WITH LABELS {label." + ssn + "." + scn + "} VISUAL(TITLE \""
                    + lcn + "\") AS {d_" + ssn + "_" + scn + "."+N.NM_PFX + scn + "};\n"
                    + "ALTER DATASET {" + schema.getDatasetName() + "} ADD {attr." + ssn + "." + scn + "};\n\n";
            return script;
        }
    }

    //facts
    private class Fact extends Column {

        Fact(SourceColumn column) {
            super(column, "fact");
        }

        @Override
        public String generateMaqlDdlAdd() {
            String folderStatement = "";
            String folder = column.getFolder();
            if (folder != null && folder.length() > 0) {
                String sfn = StringUtil.toIdentifier(folder);
                folderStatement = ", FOLDER {ffld." + sfn + "}";
            }

            return "CREATE FACT {fact." + ssn + "." + scn + "} VISUAL(TITLE \"" + lcn
                    + "\"" + folderStatement + ") AS {" + getFactTableName() + "."+N.FCT_PFX + scn + "};\n"
                    + "ALTER DATASET {" + schema.getDatasetName() + "} ADD {" + identifier + "};\n\n";
        }
    }

    //labels
    private class Label extends Column {

        Label(SourceColumn column) {
            super(column, "label");
        }

        @Override
        public String generateMaqlDdlAdd() {
            String scnPk = StringUtil.toIdentifier(column.getReference());
            Attribute attr = attributes.get(scnPk);
            
            if (attr == null) {
            	throw new IllegalArgumentException("Label " + scn + " points to non-existing attribute " + scnPk);
            }
            String script = "# ADD LABELS TO ATTRIBUTES\n";
            script += "ALTER ATTRIBUTE {attr." + ssn + "." + scnPk + "} ADD LABELS {label." + ssn + "." + scnPk + "."
                    + scn + "} VISUAL(TITLE \"" + lcn + "\") AS {" + attr.table + "."+N.NM_PFX + scn + "};\n\n";
            return script;
        }
        
        public String generateMaqlDdlDrop() {
        	throw new UnsupportedOperationException("Generate MAQL Drop is not supported for LABELS yet");
        }
    }

    // dates
    private class DateColumn extends Column {

        DateColumn(SourceColumn column) {
            super(column, N.DT);
        }

        @Override
        public String generateMaqlDdlAdd() {
            String folderStatement = "";
            String folder = column.getFolder();
            String reference = column.getSchemaReference();
            if (folder != null && folder.length() > 0) {
                String sfn = StringUtil.toIdentifier(folder);
                folderStatement = ", FOLDER {ffld." + sfn + "}";
            }
            String stat = "CREATE FACT {" + identifier + "} VISUAL(TITLE \"" + lcn
                    + "\"" + folderStatement + ") AS {" + getFactTableName() + "."+N.DT_PFX + scn + "_"+N.ID+"};\n"
                    + "ALTER DATASET {" + schema.getDatasetName() + "} ADD {"+ identifier + "};\n\n";
            if(reference != null && reference.length() > 0) {
                reference = StringUtil.toIdentifier(reference);
                stat += "# CONNECT THE DATE TO THE DATE DIMENSION\n";
                stat += "ALTER ATTRIBUTE {"+reference+"."+N.DT_ATTR_NAME+"} ADD KEYS {"+getFactTableName() + 
                        "."+N.DT_PFX + scn + "_"+N.ID+"};\n\n";
            }
            return stat;
        }
        
        public String generateMaqlDdlDrop() {
        	String script = super.generateMaqlDdlDrop();
        	String reference = column.getSchemaReference();
        	if(reference != null && reference.length() > 0) {
                reference = StringUtil.toIdentifier(reference);
                script += "# DISCONNECT THE DATE DIMENSION\n";
                script += "ALTER ATTRIBUTE {"+reference+"."+N.DT_ATTR_NAME+"} DROP KEYS {"+getFactTableName() + 
                        "."+N.DT_PFX + scn + "_"+N.ID+"};\n\n";
            }
        	return script;
        }
    }

    // connection points
    private class ConnectionPoint extends Attribute {
        public ConnectionPoint(SourceColumn column) {
            super(column);
            hasCp = true;
            //factsOfPrimaryColumn = scn + "_" + N.ID;
        }

		 @Override
		protected String createForeignKeyMaqlDdl() {
			// The fact table's primary key values are identical with the primary key values
			// of a Connection Point attribute. This is why the fact table's PK may act as 
			// the connection point's foreign key as well
			return "{" + getFactTableName() + "."+N.ID+"}";
		}
		 
		 public String generateMaqlDdlDrop() {
	      	throw new UnsupportedOperationException("Generate MAQL Drop is not supported for CONNECTION_POINTS yet");
		 }
    }

    // references
    private class Reference extends Column {
    	public Reference(SourceColumn column) {
			super(column, "");
		}
    	
    	@Override
    	public String generateMaqlDdlAdd() {
    		String foreignAttrId = createFactOfMaqlDdl(column.getSchemaReference());
            String script = "# CONNECT THE REFERENCE TO THE APPROPRIATE DIMENSION\n";
    		script += "ALTER ATTRIBUTE " + foreignAttrId
    					  + " ADD KEYS " + createForeignKeyMaqlDdl() + ";\n\n"; 
    		return script;
    	}
    	
    	public String generateMaqlDdlDrop() {
    		String foreignAttrId = createFactOfMaqlDdl(column.getSchemaReference());
            String script = "# DISCONNECT THE REFERENCE FROM THE APPROPRIATE DIMENSION\n";
    		script += "ALTER ATTRIBUTE " + foreignAttrId
    					  + " DROP KEYS " + createForeignKeyMaqlDdl() + ";\n\n"; 
    		return script;
		 }
    }   
}

