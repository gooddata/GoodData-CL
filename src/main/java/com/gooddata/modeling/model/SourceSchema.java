package com.gooddata.modeling.model;

import com.gooddata.modeling.exceptions.ModelingException;
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
     * @throws ModelingException thrown if the column doesn't exist
     */
    public SourceColumn getColumnByName(String name) throws ModelingException {
        for (SourceColumn c : columns)
            if (c.getName().equals(name))
                return c;
        throw new ModelingException("Column " + name + " not found.");

    }

}
