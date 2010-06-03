package com.gooddata.transformation.generator.model;

import com.gooddata.exceptions.ModelException;
import com.gooddata.modeling.model.SourceColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * GoodData PDM table
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class PdmTable {


    // schema types
    public final static String PDM_TABLE_TYPE_LOOKUP = "LOOKUP";
    public final static String PDM_TABLE_TYPE_FACT = "FACT";
    public final static String PDM_TABLE_TYPE_SOURCE = "SOURCE";
    public final static String PDM_TABLE_TYPE_SYSTEM = "SYSTEM";

    // PDM columns
    private List<PdmColumn> columns = new ArrayList<PdmColumn>();

    // type
    private String type;

    // name
    private String name;

    // the source column that the lookup represents
    private String representedLookupColumn;

    /**
     * Constructor
     * @param name column name
     */
    public PdmTable(String name) {
        setName(name);
    }

    /**
     * Constructor
     * @param name column name
     * @param type column type
     */
    public PdmTable(String name, String type) {
        this(name);
        setType(type);
    }

    /**
     * Constructor
     * @param name column name
     * @param type column type
     * @param lookupSourceColumn source column that the lookup represents
     */
    public PdmTable(String name, String type, String lookupSourceColumn) {
        this(name, type);
        setRepresentedLookupColumn(lookupSourceColumn);
    }
   
    /**
     * Columns getter
     * @return the PDM columns
     */
    public List<PdmColumn> getColumns() {
        return columns;
    }

    /**
     * Columns setter
     * @param columns schema columns
     */
    public void setColumns(List<PdmColumn> columns) {
        this.columns = columns;
    }

    /**
     * Type getter
     * @return the PDM type
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
     * Adds a new column
     * @param col new column
     */
    public void addColumn(PdmColumn col) {
        this.columns.add(col);
    }

    /**
     * Returns all columns that are represented in the source table
     * @return all columns that are represented in the source table
     */
    public List<PdmColumn> getRepresentingColumns() {
        List<PdmColumn> cols = new ArrayList<PdmColumn>();
        for(PdmColumn col : getColumns()) {
            if(col != null && col.getSourceColumn() != null) {
                cols.add(col);
            }
        }
        return cols;
    }


    /**
     * Returns all columns that are represented in the source table
     * @param ldmReference the LDM reference to search for
     * @return all columns that represent the LDM type
     */
    protected List<PdmColumn> getColumnsByLdmReference(String ldmReference) {
        List<PdmColumn> cols = new ArrayList<PdmColumn>();
        for(PdmColumn col : getColumns()) {
            if(col != null && ldmReference.equals(col.getLdmTypeReference())) {
                cols.add(col);
            }
        }
        return cols;
    }

    /**
     * Returns table's fact columns
     * @return table's fact columns
     */
    public List<PdmColumn> getFactColumns() {
        return getColumnsByLdmReference(SourceColumn.LDM_TYPE_FACT);
    }

    /**
     * Returns table's attribute columns
     * @return table's attribute columns
     */
    public List<PdmColumn> getAttributeColumns() {
        return getColumnsByLdmReference(SourceColumn.LDM_TYPE_ATTRIBUTE);
    }

    /**
     * Returns table's date columns
     * @return table's datecolumns
     */
    public List<PdmColumn> getDateColumns() {
        return getColumnsByLdmReference(SourceColumn.LDM_TYPE_DATE);
    }

    /**
     * Returns column by name
     * @param name the name of the column
     * @return the column with the desired name
     * @throws ModelException if there is no column with such name
     */
    public PdmColumn getColumnByName(String name) throws ModelException {
        for(PdmColumn c : getColumns()) {
            if(c.getName().equals(name))
                return c;
        }
        throw new ModelException("Column with name '" + name + "' doesn't exist.");
    }

    /**
     * Returns source column that the lookup represents
     * @return source column that the lookup represents
     */
    public String getRepresentedLookupColumn() {
        return representedLookupColumn;
    }

    /**
     * Sets source column that the lookup represents
     * @param representedLookupColumn source column that the lookup represents
     */
    public void setRepresentedLookupColumn(String representedLookupColumn) {
        this.representedLookupColumn = representedLookupColumn;
    }
}
