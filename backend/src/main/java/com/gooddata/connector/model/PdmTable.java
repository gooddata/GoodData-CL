/*
 * Copyright (c) 2009 GoodData Corporation.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Redistributions in any form must be accompanied by information on
 *    how to obtain complete source code for this software and any
 *    accompanying software that uses this software.  The source code
 *    must either be included in the distribution or be available for no
 *    more than the cost of distribution plus a nominal fee, and must be
 *    freely redistributable under reasonable conditions.  For an
 *    executable file, complete source code means the source code for all
 *    modules it contains.  It does not include source code for modules or
 *    files that typically accompany the major components of the operating
 *    system on which the executable file runs.
 *
 * THIS SOFTWARE IS PROVIDED BY GOODDATA ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT, ARE DISCLAIMED.  IN NO EVENT SHALL ORACLE BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.gooddata.connector.model;

import com.gooddata.exception.ModelException;
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
    public final static String PDM_TABLE_TYPE_REFERENCE = "REFERENCE";
    public final static String PDM_TABLE_TYPE_CONNECTION_POINT = "CONNECTION_POINT";

    // PDM columns
    private List<PdmColumn> columns = new ArrayList<PdmColumn>();

    // type
    private String type;

    // name
    private String name;

    // the source column that the lookup represents
    private String associatedSourceColumn;
    
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
        setAssociatedSourceColumn(lookupSourceColumn);
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
    public List<PdmColumn> getAssociatedColumns() {
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
        throw new ModelException("Column with name '" + name + "' doesn't exist in table '" + getName() + "'.");
    }

    /**
     * Returns source column that the lookup represents
     * @return source column that the lookup represents
     */
    public String getAssociatedSourceColumn() {
        return associatedSourceColumn;
    }

    /**
     * Sets source column that the lookup represents
     * @param associatedSourceColumn source column that the lookup represents
     */
    public void setAssociatedSourceColumn(String associatedSourceColumn) {
        this.associatedSourceColumn = associatedSourceColumn;
    }

    @Override
	public String toString() {
		return name + "(" + type + ")";
	}
}
