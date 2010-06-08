package com.gooddata.transformation.executor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * GoodData PDM column
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class PdmColumn {


    // data types
    public static final String PDM_COLUMN_TYPE_TEXT = "VARCHAR(255)";
    public static final String PDM_COLUMN_TYPE_LONG_TEXT = "VARCHAR(255)";
    public static final String PDM_COLUMN_TYPE_DATE = "INT";
    public static final String PDM_COLUMN_TYPE_INT = "INT";
    public static final String PDM_COLUMN_TYPE_LONG = "BIGINT";
    public static final String PDM_COLUMN_TYPE_FLOAT = "DECIMAL(15,4)";    

    // constraints
    public static final String PDM_CONSTRAINT_INDEX_UNIQUE = "UNIQUE";
    public static final String PDM_CONSTRAINT_INDEX_MULTIPLE = "MULTIPLE";
    public static final String PDM_CONSTRAINT_AUTOINCREMENT = "AUTOINCREMENT";
    public static final String PDM_CONSTRAINT_PK = "PRIMARY KEY";

    //name
    private String name;

    // type
    private String type;

    // source column that the column sourceColumn
    private String sourceColumn;

    // column constraints
    private List<String> constraints = new ArrayList<String>();

    // LDM type reference
    private String ldmTypeReference;

    //data format
    private String format;



    /**
     * Constructor
     * @param name column name
     * @param type column type
     */
    public PdmColumn(String name, String type) {
        setName(name);
        setType(type);
    }

    /**
     * Constructor
     * @param name column name
     * @param type column type
     * @param constraints column constraints
     */
    public PdmColumn(String name, String type, String[] constraints) {
        this(name, type);
        if(constraints != null && constraints.length > 0)
            for(String c : constraints)
                this.constraints.add(c);
    }

    /**
     * Constructor
     * @param name column name
     * @param type column type
     * @param constraints column constraints
     * @param sourceColumn the source column that the column sourceColumn
     */
    public PdmColumn(String name, String type, String[] constraints, String sourceColumn) {
        this(name, type, constraints);
        setSourceColumn(sourceColumn);
    }

    /**
     * Constructor
     * @param name column name
     * @param type column type
     * @param sourceColumn the source column that the column sourceColumn
     */
    public PdmColumn(String name, String type, String sourceColumn) {
        this(name, type);
        setSourceColumn(sourceColumn);
    }

    /**
     * Constructor
     * @param name column name
     * @param type column type
     * @param constraints column constraints
     * @param sourceColumn the source column that the column sourceColumn
     * @param ldmTypeReference LDM type that the column represents (ATTRIBUTE | LABEL | FACT)
     */
    public PdmColumn(String name, String type, String[] constraints, String sourceColumn, String ldmTypeReference) {
        this(name, type, constraints);
        setSourceColumn(sourceColumn);
        setLdmTypeReference(ldmTypeReference);

    }

    /**
     * Constructor
     * @param name column name
     * @param type column type
     * @param constraints column constraints
     * @param sourceColumn the source column that the column sourceColumn
     * @param ldmTypeReference LDM type that the column represents (ATTRIBUTE | LABEL | FACT)
     * @param format column format (e.g. for date columns) 
     */
    public PdmColumn(String name, String type, String[] constraints, String sourceColumn, String ldmTypeReference,
                     String format) {
        this(name, type, constraints, sourceColumn, ldmTypeReference);
        setFormat(format);
    }

    /**
     * Constructor
     * @param name column name
     * @param type column type
     * @param sourceColumn the source column that the column sourceColumn
     * @param ldmTypeReference LDM type that the column represents (ATTRIBUTE | LABEL | FACT) 
     */
    public PdmColumn(String name, String type, String sourceColumn, String ldmTypeReference) {
        this(name, type);
        setSourceColumn(sourceColumn);
        setLdmTypeReference(ldmTypeReference);
    }

    /**
     * Constructor
     * @param name column name
     * @param type column type
     * @param sourceColumn the source column that the column sourceColumn
     * @param ldmTypeReference LDM type that the column represents (ATTRIBUTE | LABEL | FACT)
     * @param format column format (e.g. for date columns)
     */
    public PdmColumn(String name, String type, String sourceColumn, String ldmTypeReference, String format) {
        this(name, type, sourceColumn, ldmTypeReference);
        setFormat(format);
    }


    /**
     * Name getter
     * @return column name
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter
     * @param name column name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Type getter
     * @return column type
     */
    public String getType() {
        return type;
    }

    /**
     * Type setter
     * @param type column type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Represented source column getter
     * @return the source column that the column sourceColumn
     */
    public String getSourceColumn() {
        return sourceColumn;
    }

    /**
     * Represented source column setter
     * @param sourceColumn represented source column
     */
    public void setSourceColumn(String sourceColumn) {
        this.sourceColumn = sourceColumn;
    }

    /**
     * Constraints getter
     * @return column constraints
     */
    public List<String> getConstraints() {
        return constraints;
    }

    /**
     * Constraints setter
     * @param constraints column constraints
     */
    public void setConstraints(List<String> constraints) {
        this.constraints = constraints;
    }

    /**
     * LDM type reference getter
     * @return ldm type reference
     */
    public String getLdmTypeReference() {
        return ldmTypeReference;
    }

    /**
     * LDM type reference setter
     * @param ldmTypeReference LDM type that the column represents (ATTRIBUTE | LABEL | FACT)
     */
    public void setLdmTypeReference(String ldmTypeReference) {
        this.ldmTypeReference = ldmTypeReference;
    }

    /**
     * True if the column is primary key
     * @return true if the column is primary key
     */
    public boolean isPrimaryKey() {
        for(String constraint : getConstraints())
            if(PDM_CONSTRAINT_PK.equals(constraint))
                return true;
        return false;
    }

    /**
     * True if the column is AUTOINCREMENT
     * @return true if the column is AUTOINCREMENT
     */
    public boolean isAutoIncrement() {
        for(String constraint : getConstraints())
            if(PDM_CONSTRAINT_AUTOINCREMENT.equals(constraint))
                return true;
        return false;
    }

    /**
     * True if the column is UNIQUE
     * @return true if the column is UNIQUE
     */
    public boolean isUnique() {
        for(String constraint : getConstraints())
            if(PDM_CONSTRAINT_INDEX_UNIQUE.equals(constraint))
                return true;
        return false;
    }

    /**
     * Format getter
     * @return column format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Format setter
     * @param format column format
     */
    public void setFormat(String format) {
        this.format = format;
    }
}
