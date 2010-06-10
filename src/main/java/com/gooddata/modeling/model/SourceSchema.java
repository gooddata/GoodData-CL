package com.gooddata.modeling.model;

import com.gooddata.exceptions.ModelException;
import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GoodData source schema
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class SourceSchema {

    /**
     * The LDM schema name
     */
    private String name;

    /**
     * Schema columns
     */
    private List<SourceColumn> columns;

    /**
     * Constructor
     * @param name schema name
     */
    protected SourceSchema(String name) {
        this.name = name;
        columns = new ArrayList<SourceColumn>();
    }

    /**
     * Creates a new empty schema
     * @param name the schema name
     * @return new SourceSchema
     */
    public static SourceSchema createSchema(String name) {
        return new SourceSchema(name);
    }


    /**
     * Creates a new SourceSchema from the XML config file
     * @param configFile the config file
     * @return new SourceSchema
     * @throws IOException in case of an IO issue 
     */
    public static SourceSchema createSchema(File configFile) throws IOException {
        return fromXml(configFile);
    }

    /**
     * Serializes the schema to XML
     * @return the xml representation of the object
     * @throws IOException in case of an IO issue
     */
    protected String toXml() throws IOException {
        XStream xstream = new XStream();
        xstream.alias("column", SourceColumn.class);
        xstream.alias("schema", SourceSchema.class);
        return xstream.toXML(this);
    }

    /**
     * Deserializes the schema from XML
     * @param configFile  the file with the XML definition
     * @throws IOException in case of an IO issue
     */
    protected static SourceSchema fromXml(File configFile) throws IOException {
        XStream xstream = new XStream();
        xstream.alias("column", SourceColumn.class);
        xstream.alias("schema", SourceSchema.class);
        FileReader r = new FileReader(configFile);
        SourceSchema schema = (SourceSchema)xstream.fromXML(r);
        r.close();
        return schema;
    }

    /**
     * Write the config file
     * @param configFile  the config file
     * @throws IOException in case of an IO issue
     */
    public void writeConfig(File configFile) throws IOException {
        FileWriter w = new FileWriter(configFile);
        w.write(toXml());
        w.flush();
        w.close();
    }

    /**
     * Columns getter
     * @return the List of all columns
     */
    public List<SourceColumn> getColumns() {
        return columns;
    }

    /**
     * Columns setter
     * @param columns the List of columns
     */
    public void setColumns(List<SourceColumn> columns) {
        this.columns = columns;
    }

    /**
     * Adds a new column
     * @param c the new column
     */
    public void addColumn(SourceColumn c) {
        this.columns.add(c);
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
     * Returns a column by it's name
     * @param name name to search for (case sensitive)
     * @return the matching column
     * @throws com.gooddata.exceptions.ModelException thrown if the column doesn't exist
     */
    public SourceColumn getColumnByName(String name) throws ModelException {
        for (SourceColumn c : columns)
            if (c.getName().equals(name))
                return c;
        throw new ModelException("Column " + name + " not found.");

    }

    /**
     * Returns a column by it's type
     * @param type type to search for (case sensitive)
     * @return the matching columns
     * @throws com.gooddata.exceptions.ModelException thrown if the column doesn't exist
     */
    public List<SourceColumn> getColumnByType(String type) {
        ArrayList<SourceColumn> l = new ArrayList<SourceColumn>();
        for (SourceColumn c : columns)
            if (type.equals(c.getLdmType()))
                l.add(c);
        return l;
    }

    /**
     * Returns all LABEL columns
     * @return all LABEL columns
     */
    public List<SourceColumn> getLabels() {
        return getColumnByType(SourceColumn.LDM_TYPE_LABEL);
    }

    /**
     * Returns all ATTRIBUTE columns
     * @return all ATTRIBUTE columns
     */
    public List<SourceColumn> getAttributes() {
        return getColumnByType(SourceColumn.LDM_TYPE_ATTRIBUTE);
    }

    /**
     * Returns all FACT columns
     * @return all FACT columns
     */
    public List<SourceColumn> getFacts() {
        return getColumnByType(SourceColumn.LDM_TYPE_FACT);
    }

    /**
     * Returns all REFERENCE columns
     * @return all REFERENCE columns
     */
    public List<SourceColumn> getReferences() {
        return getColumnByType(SourceColumn.LDM_TYPE_REFERENCE);
    }

    /**
     * Returns all CONNECTION POINT columns
     * @return all CONNECTION POINT columns
     */
    public List<SourceColumn> getConnectionPoints() {
        return getColumnByType(SourceColumn.LDM_TYPE_CONNECTION_POINT);
    }

    /**
     * Returns all DATE columns
     * @return all DATE POINT columns
     */
    public List<SourceColumn> getDates() {
        return getColumnByType(SourceColumn.LDM_TYPE_DATE);
    }

}
