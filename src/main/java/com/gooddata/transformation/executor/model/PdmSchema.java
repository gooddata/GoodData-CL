package com.gooddata.transformation.executor.model;

import com.gooddata.exceptions.ModelException;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * GoodData PDM Schema
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class PdmSchema {

    // tables
    private List<PdmTable> tables = new ArrayList<PdmTable>();

    // schema name
    private String name;

    // lokup replication for computing ocnsistent REFERENCE IDs
    private List<PdmLookupReplication> lookupReplications = new ArrayList<PdmLookupReplication>(); 


    /**
     * Constructor
     * @param name schema name
     */
    protected PdmSchema(String name) {
        setName(name);
    }

    /**
     * Creates schema from a SourceSchema
     * @param schema the SourceSchema
     * @return the new PdmSchema
     */
    public static PdmSchema createSchema(SourceSchema schema) {
        PdmSchema pdm = new PdmSchema(StringUtil.formatShortName(schema.getName()));

        PdmTable source = new PdmTable("o_"+pdm.getName(), PdmTable.PDM_TABLE_TYPE_SOURCE);
        source.addColumn(new PdmColumn("o_genid",PdmColumn.PDM_COLUMN_TYPE_INT,
                new String[] {PdmColumn.PDM_CONSTRAINT_AUTOINCREMENT, PdmColumn.PDM_CONSTRAINT_PK}));
        PdmTable fact = new PdmTable("f_"+pdm.getName(), PdmTable.PDM_TABLE_TYPE_FACT);
        fact.addColumn(new PdmColumn("id",PdmColumn.PDM_COLUMN_TYPE_INT,
                new String[] {PdmColumn.PDM_CONSTRAINT_PK}, "o_genid"));

        HashMap<String, List<SourceColumn>> lookups = new HashMap<String, List<SourceColumn>>();

        for (SourceColumn column : schema.getColumns()) {
            String scn = StringUtil.formatShortName(column.getName());
            String ssn = StringUtil.formatShortName(pdm.getName());
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_ATTRIBUTE) ||
                    column.getLdmType().equals(SourceColumn.LDM_TYPE_REFERENCE)) {
                source.addColumn(new PdmColumn("o_" + scn, PdmColumn.PDM_COLUMN_TYPE_TEXT));
                fact.addColumn(new PdmColumn(scn+"_id", PdmColumn.PDM_COLUMN_TYPE_INT, "d_" + ssn + "_"+scn +
                        ".id", SourceColumn.LDM_TYPE_ATTRIBUTE));
                // add lookup tables
                if (!lookups.containsKey(scn)) {
                    lookups.put(scn, new ArrayList<SourceColumn>());
                }
                // add column to the lookup
                List<SourceColumn> l = lookups.get(scn);
                l.add(column);
                // process references in a same way as the attributes.
                // just copy the referenced lookup rows to the referencing lookup
                if (column.getLdmType().equals(SourceColumn.LDM_TYPE_REFERENCE)) {
                    String tcn = StringUtil.formatShortName(column.getPk());
                    String tsn = StringUtil.formatShortName(column.getPkSchema());
                    pdm.addLookupReplication(new PdmLookupReplication("d_" + tsn +
                    "_"+tcn, "nm_" + tcn, "d_" + ssn + "_"+scn, "nm_" + scn));
                }
            }
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_LABEL)) {
                source.addColumn(new PdmColumn("o_" + scn, PdmColumn.PDM_COLUMN_TYPE_TEXT));
                String scnPk = StringUtil.formatShortName(column.getPk());
                if (!lookups.containsKey(scnPk)) {
                    lookups.put(scnPk, new ArrayList<SourceColumn>());
                }
                List<SourceColumn> l = lookups.get(scnPk);
                l.add(column);
            }
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_FACT)) {
                source.addColumn(new PdmColumn("o_" + scn, PdmColumn.PDM_COLUMN_TYPE_TEXT));
                fact.addColumn(new PdmColumn("f_" + scn, PdmColumn.PDM_COLUMN_TYPE_TEXT, "o_" + scn,
                        SourceColumn.LDM_TYPE_FACT));
            }
            if (column.getLdmType().equals(SourceColumn.LDM_TYPE_DATE)) {
                source.addColumn(new PdmColumn("o_" + scn, PdmColumn.PDM_COLUMN_TYPE_TEXT));
                fact.addColumn(new PdmColumn("dt_" + scn + "_id", PdmColumn.PDM_COLUMN_TYPE_DATE, "o_" + scn,
                        SourceColumn.LDM_TYPE_DATE, column.getFormat()));
            }
            
        }

        pdm.addTable(source);
        pdm.addTable(fact);

        for (String column : lookups.keySet()) {
            PdmTable lookup = new PdmTable("d_" + pdm.getName() + "_" + column, PdmTable.PDM_TABLE_TYPE_LOOKUP, column);
            lookup.addColumn(new PdmColumn("id", PdmColumn.PDM_COLUMN_TYPE_INT,
                    new String[] {PdmColumn.PDM_CONSTRAINT_AUTOINCREMENT, PdmColumn.PDM_CONSTRAINT_PK}));
            lookup.addColumn(new PdmColumn("hashid", PdmColumn.PDM_COLUMN_TYPE_LONG_TEXT,
                    new String[] {PdmColumn.PDM_CONSTRAINT_INDEX_UNIQUE}));
            List<SourceColumn> l = lookups.get(column);
            for (SourceColumn c : l) {
                String scnNm = StringUtil.formatShortName(c.getName());
                lookup.addColumn(new PdmColumn("nm_" + scnNm, PdmColumn.PDM_COLUMN_TYPE_TEXT, "o_" + scnNm,
                        SourceColumn.LDM_TYPE_ATTRIBUTE));
            }
            pdm.addTable(lookup);
        }

        return pdm;
    }


    /**
     * Tables getter
     * @return schema tables
     */
    public List<PdmTable> getTables() {
        return tables;
    }

    /**
     * Tables setter
     * @param tables schema tables
     */
    public void setTables(List<PdmTable> tables) {
        this.tables = tables;
    }

    /**
     * Name getter
     * @return schema name
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter
     * @param name schema name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds new table to the schema
     * @param tbl new table
     */
    public void addTable(PdmTable tbl) {
        this.tables.add(tbl);
    }

    /**
     * Return all schema lookup tables
     * @param type the PDM table type
     * @return all schema lookup tables
     */
    protected List<PdmTable> getTablesByType(String type) {
        List<PdmTable> tbls = new ArrayList<PdmTable>();
        for(PdmTable table : getTables()) {
            if(table != null && type.equals(table.getType())) {
                tbls.add(table);
            }
        }
        return tbls;
    }

    /**
     * Return table by name
     * @param name the PDM table name
     * @return the table
     * @throws ModelException if the table doesn't exist
     */
    public PdmTable getTableByName(String name) throws ModelException {
        for(PdmTable table : getTables()) {
            if(table != null && name.equals(table.getName()))
                return table;
        }
        throw new ModelException("Table '" + name + "' doesn't exist.");    
    }

    /**
     * Returns all lookup tables
     * @return all lookup tables
     */
    public List<PdmTable> getLookupTables() {
        return this.getTablesByType(PdmTable.PDM_TABLE_TYPE_LOOKUP);
    }

    /**
     * Returns fact table
     * @return fact table
     * @throws ModelException if there is no or too many fact tables
     */
    public PdmTable getFactTable() throws ModelException {
        List<PdmTable> l = this.getTablesByType(PdmTable.PDM_TABLE_TYPE_FACT);
        if(l.size() == 1) {
            return l.get(0);
        }
        if(l.size() <=0)
            throw new ModelException("No fact table found in the dataset.");
        throw new ModelException("Multiple fact tables found in the dataset.");
    }

    /**
     * Returns source tables
     * @return source table
     * @throws ModelException if there is no or too many fact tables
     */
    public PdmTable getSourceTable() throws ModelException {
        List<PdmTable> l = this.getTablesByType(PdmTable.PDM_TABLE_TYPE_SOURCE);
        if(l.size() == 1) {
            return l.get(0);
        }
        if(l.size() <=0)
            throw new ModelException("No source table found in the dataset.");
        throw new ModelException("Multiple source tables found in the dataset.");
    }

    /**
     * Returns the active lookup replications
     * @return active lookup replications
     */
    public List<PdmLookupReplication> getLookupReplications() {
        return lookupReplications;
    }

    /**
     * Lookup replications setter
     * @param lookupReplications lookupReplications list
     */
    public void setLookupReplications(List<PdmLookupReplication> lookupReplications) {
        this.lookupReplications = lookupReplications;
    }

    /**
     * Ads a new lookup replication
     * @param r new lookup replication
     */
    public void addLookupReplication(PdmLookupReplication r) {
        this.getLookupReplications().add(r);
    }

}
