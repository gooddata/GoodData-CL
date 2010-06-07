package com.gooddata.transformation.executor.model;

/**
 * PdmLookupReplication holds the data for replication of the lookup tables.
 * User can setup a reference from a schema column from another schema. The reference is processed as a regular
 * attribute with just one exception. The attribute's lookup is populated with the referenced schema column's lookup
 * rows before the normalization. This makes sure that the column values are translated to the consistent IDs for both
 * schema columns.
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class PdmLookupReplication {

    // the referenced PDM lookup table
    private String referencedLookup;
    // the referencing PDM lookup table
    private String referencingLookup;
    // the referenced PDM lookup table's name (value) column
    private String referencedColumn;
    // the referencing PDM lookup table's name (value) column
    private String referencingColumn;


    /**
     * Constructor
     * @param referencedLookup referenced lookup
     * @param referencedColumn referenced lookup's name/value (not id) column
     * @param referencingLookup referencing lookup
     * @param referencingColumn referencing lookup's name/value (not id) column
     */
    public PdmLookupReplication(String referencedLookup, String referencedColumn, String referencingLookup,
                                String referencingColumn) {
        setReferencedLookup(referencedLookup);
        setReferencingLookup(referencingLookup);
        setReferencedColumn(referencedColumn);
        setReferencingColumn(referencingColumn);
    }

    /**
     * Referenced lookup table name getter
     * @return referenced lookup table name
     */
    public String getReferencedLookup() {
        return referencedLookup;
    }

    /**
     * Referenced lookup table name setter
     * @param referencedLookup referenced lookup table name
     */
    public void setReferencedLookup(String referencedLookup) {
        this.referencedLookup = referencedLookup;
    }

    /**
     * Referencing lookup table name getter
     * @return referencing lookup table name
     */
    public String getReferencingLookup() {
        return referencingLookup;
    }

    /**
     * Referencing lookup table name setter
     * @param referencingLookup referencing lookup table name
     */
    public void setReferencingLookup(String referencingLookup) {
        this.referencingLookup = referencingLookup;
    }

    /**
     * Referenced (name / value not the id) column getter
     * @return referenced (name / value not the id) column
     */
    public String getReferencedColumn() {
        return referencedColumn;
    }

    /**
     * Referenced (name / value not the id) column setter
     * @param referencedColumn Referenced (name / value not the id) column setter
     */
    public void setReferencedColumn(String referencedColumn) {
        this.referencedColumn = referencedColumn;
    }
    /**
     * Referencing (name / value not the id) column getter
     * @return referencing (name / value not the id) column
     */
    public String getReferencingColumn() {
        return referencingColumn;
    }

    /**
     * Referencing (name / value not the id) column setter
     * @param referencingColumn Referencing (name / value not the id) column setter
     */
    public void setReferencingColumn(String referencingColumn) {
        this.referencingColumn = referencingColumn;
    }
}
