package com.gooddata.connector.model;

import com.gooddata.exceptions.ModelException;
import com.gooddata.modeling.model.SourceColumn;
import com.gooddata.modeling.model.SourceSchema;
import com.gooddata.naming.N;
import com.gooddata.util.StringUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * GoodData PDM Schema
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class PdmSchema {

    private static Logger l = Logger.getLogger(PdmSchema.class);

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
     * @param srcSchema the SourceSchema
     * @return the new PdmSchema
     */
    public static PdmSchema createSchema(SourceSchema srcSchema) throws ModelException {
        String schemaName = StringUtil.formatShortName(srcSchema.getName());
        PdmSchema schema = new PdmSchema(schemaName);

        PdmTable sourceTable = createSourceTable(schemaName);
        schema.addTable(sourceTable);
        PdmTable factTable =   createFactTable(schemaName);
        schema.addTable(factTable);

        // we will add all LABELs to this list to process it later
        List<SourceColumn> labels = new ArrayList<SourceColumn>();

        // we need to process columns in sequence
        for (SourceColumn column : srcSchema.getColumns()) {
            if(SourceColumn.LDM_TYPE_ATTRIBUTE.equals(column.getLdmType())) {
                sourceTable.addColumn(createSourceColumn(column));
                factTable.addColumn(createFactColumn(column, schemaName));
                addLookupColumn(schema, column, PdmTable.PDM_TABLE_TYPE_LOOKUP);
            }
            if(SourceColumn.LDM_TYPE_CONNECTION_POINT.equals(column.getLdmType())) {
                sourceTable.addColumn(createSourceColumn(column));
                factTable.addColumn(createFactColumn(column, schemaName));
                addLookupColumn(schema, column, PdmTable.PDM_TABLE_TYPE_CONNECTION_POINT);
            }

            if(SourceColumn.LDM_TYPE_REFERENCE.equals(column.getLdmType())) {
                sourceTable.addColumn(createSourceColumn(column));
                factTable.addColumn(createFactColumn(column, schemaName));
                addLookupColumn(schema, column, PdmTable.PDM_TABLE_TYPE_REFERENCE);
                // just copy the referenced lookup rows to the referencing lookup
                createTableReplication(schema, column);
            }
            if(SourceColumn.LDM_TYPE_FACT.equals(column.getLdmType())) {
                sourceTable.addColumn(createSourceColumn(column));
                factTable.addColumn(createFactColumn(column, schemaName));
            }
            if(SourceColumn.LDM_TYPE_DATE.equals(column.getLdmType())) {
                sourceTable.addColumn(createSourceColumn(column));
                factTable.addColumn(createFactColumn(column, schemaName));
            }

            if(SourceColumn.LDM_TYPE_LABEL.equals(column.getLdmType())) {
                sourceTable.addColumn(createSourceColumn(column));
                labels.add(column);
            }

        }
        // we need to process LABELs later when all lookups are created
        // LABEL definition can precede it's primary attribute definition
        for(SourceColumn column: labels) {
            String pkName = StringUtil.formatShortName(column.getReference());
            PdmTable lookup = schema.getTableByName(createLookupTableName(schemaName, pkName));
            lookup.addColumn(createLookupColumn(column));
        }

        return schema;
    }

    private static void addLookupColumn(PdmSchema s, SourceColumn c, String tblType) {
        String cName = StringUtil.formatShortName(c.getName());
        String sName = s.getName();
        String tableName = createLookupTableName(sName, cName);
                if(!s.contains(tableName))
                    s.addTable(createLookupTable(sName, cName, tblType));
        PdmTable lookup = null;
        try {
            lookup = s.getTableByName(tableName);
        } catch (ModelException e) {
            l.error("Intenal problem: schema contains a table but it can't find it.");
        }
        lookup.addColumn(createLookupColumn(c));
    }

    private static void createTableReplication(PdmSchema s, SourceColumn c) {
        String sName = s.getName();
        String cName = StringUtil.formatShortName(c.getName());
        String tcn = StringUtil.formatShortName(c.getReference());
                String tsn = StringUtil.formatShortName(c.getSchemaReference());
                s.addLookupReplication(new PdmLookupReplication(createLookupTableName(tsn,tcn),
                        N.NM_PFX + tcn, createLookupTableName(sName,cName),
                        N.NM_PFX + cName));
    }

    private static PdmColumn createLookupColumn(SourceColumn c) {
        String name = StringUtil.formatShortName(c.getName());
        return new PdmColumn(N.NM_PFX + name, PdmColumn.PDM_COLUMN_TYPE_TEXT,
                N.SRC_PFX + name, c.getLdmType());
    }

    private static PdmTable createLookupTable(String schemaName, String columnName, String tableType) {
        PdmTable lookup = new PdmTable(createLookupTableName(schemaName, columnName),tableType, columnName);
        lookup.addColumn(new PdmColumn(N.ID, PdmColumn.PDM_COLUMN_TYPE_INT,
            new String[] {PdmColumn.PDM_CONSTRAINT_AUTOINCREMENT, PdmColumn.PDM_CONSTRAINT_PK}));
        lookup.addColumn(new PdmColumn(N.HSH, PdmColumn.PDM_COLUMN_TYPE_LONG_TEXT,
            new String[] {PdmColumn.PDM_CONSTRAINT_INDEX_UNIQUE}));
        return lookup;
    }

    private static String createLookupTableName(String schemaName, String columnName) {
        return N.LKP_PFX + schemaName + "_" + columnName;
    }

    private static PdmTable createSourceTable(String schemaName) {
        PdmTable sourceTable = new PdmTable(N.SRC_PFX + schemaName, PdmTable.PDM_TABLE_TYPE_SOURCE);
        // add the source table PK
        sourceTable.addColumn(new PdmColumn(N.SRC_ID,PdmColumn.PDM_COLUMN_TYPE_INT,
                new String[] {PdmColumn.PDM_CONSTRAINT_AUTOINCREMENT, PdmColumn.PDM_CONSTRAINT_PK}));
        return sourceTable;
    }

    private static PdmTable createFactTable(String schemaName) {
        PdmTable factTable = new PdmTable(N.FCT_PFX + schemaName, PdmTable.PDM_TABLE_TYPE_FACT);
        // add the fact table PK
        factTable.addColumn(new PdmColumn(N.ID,PdmColumn.PDM_COLUMN_TYPE_INT,
                        new String[] {PdmColumn.PDM_CONSTRAINT_PK}));
        return factTable;
    }

    private static PdmColumn createSourceColumn(SourceColumn c) {
        String name = StringUtil.formatShortName(c.getName());
        return new PdmColumn(N.SRC_PFX + name, PdmColumn.PDM_COLUMN_TYPE_TEXT);
    }

    private static PdmColumn createFactColumn(SourceColumn c, String schemaName) throws ModelException {
        String name = StringUtil.formatShortName(c.getName());
        String type = c.getLdmType();
        if(type.equals(SourceColumn.LDM_TYPE_ATTRIBUTE) || type.equals(SourceColumn.LDM_TYPE_CONNECTION_POINT) ||
                type.equals(SourceColumn.LDM_TYPE_REFERENCE))
            return new PdmColumn(name+"_"+N.ID, PdmColumn.PDM_COLUMN_TYPE_INT,
                        N.LKP_PFX + schemaName + "_"+name +"." + N.ID, type);
        else if(type.equals(SourceColumn.LDM_TYPE_FACT))
            return new PdmColumn(N.FCT_PFX + name, PdmColumn.PDM_COLUMN_TYPE_TEXT,
                    N.SRC_PFX + name, type);
        else if(type.equals(SourceColumn.LDM_TYPE_DATE))
            return new PdmColumn(N.DT_PFX + name + "_"+N.ID,
                    PdmColumn.PDM_COLUMN_TYPE_INT, N.SRC_PFX + name, type, c.getFormat());
        else throw new ModelException("Unknown source column type: "+type);
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
     * Return true if the schema contains table with the specified name, false otherwise
     * @param name the PDM table name
     * @return true if the schema contains table with the specified name, false otherwise
     * @throws ModelException if the table doesn't exist
     */
    public boolean contains(String name){
        for(PdmTable table : getTables()) {
            if(table != null && name.equals(table.getName()))
                return true;
        }
        return false;    
    }

    /**
     * Returns all lookup tables
     * @return all lookup tables
     */
    public List<PdmTable> getLookupTables() {
        return this.getTablesByType(PdmTable.PDM_TABLE_TYPE_LOOKUP);
    }

    /**
     * Returns all reference lookup tables
     * @return all reference lookup tables
     */
    public List<PdmTable> getReferenceTables() {
        return this.getTablesByType(PdmTable.PDM_TABLE_TYPE_REFERENCE);
    }

    /**
     * Returns all connection point lookup tables
     * @return all connection point lookup tables
     */
    public List<PdmTable> getConnectionPointTables() {
        return this.getTablesByType(PdmTable.PDM_TABLE_TYPE_CONNECTION_POINT);
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
