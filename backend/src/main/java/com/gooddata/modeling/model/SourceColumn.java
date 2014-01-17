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

package com.gooddata.modeling.model;

import com.gooddata.exception.ModelException;
import com.gooddata.util.StringUtil;

/**
 * GoodData LDM schema column
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class SourceColumn {

    // metadata names
    public final static String LDM_TYPE_ATTRIBUTE = "ATTRIBUTE";
    public final static String LDM_TYPE_FACT = "FACT";
    public final static String LDM_TYPE_LABEL = "LABEL";
    public final static String LDM_TYPE_DATE = "DATE";
    public static final String LDM_TYPE_CONNECTION_POINT = "CONNECTION_POINT";
    public static final String LDM_TYPE_REFERENCE = "REFERENCE";
    public static final String LDM_TYPE_HYPERLINK = "HYPERLINK";

    public static final String LDM_SORT_ORDER_ASC = "ASC";

    public static final String LDM_SORT_ORDER_DESC = "DESC";

    public static final String LDM_TYPE_IGNORE = "IGNORE";

    public static final String LDM_IDENTITY = "IDENTITY";

    public static final String IDENTITY_DATATYPE = "VARCHAR(32)";

    private String name;

    private String title;

    private String ldmType;

    private String reference;

    private String schemaReference;

    private String folder;

    private String format;

    private String dataType;

    private String datetime;

    private String transformation;

    private boolean isDateFact;

    private boolean isTimeFact;

    private String sortLabel;

    private String sortOrder = LDM_SORT_ORDER_ASC;


    /**
     * SourceColumn constructor
     *
     * @param name     columna name
     * @param ldmType  column LDM type
     * @param title    column type
     * @param folder   column folder
     * @param pk       LABEL's OR REFERENCE's primary source column
     * @param pkSchema LABEL's OR REFERENCE's primary source schema
     */
    public SourceColumn(String name, String ldmType, String title, String folder, String pk, String pkSchema) {
        this(name, ldmType, title, folder, pk);
        setSchemaReference(pkSchema);
    }

    /**
     * SourceColumn constructor
     *
     * @param name    columna name
     * @param ldmType column LDM type
     * @param title   column type
     * @param folder  column folder
     * @param pk      LABELs primary attribute
     */
    public SourceColumn(String name, String ldmType, String title, String folder, String pk) {
        setName(name);
        setTitle(title);
        setFolder(folder);
        setLdmType(ldmType);
        setReference(pk);
    }

    /**
     * Constructor
     *
     * @param name    column name
     * @param ldmType LDM type
     * @param title   title
     * @param folder  enclosing folder
     * @throws com.gooddata.exception.ModelException
     *          issue with a model consistency
     */
    public SourceColumn(String name, String ldmType, String title, String folder) {
        this(name, ldmType, title, folder, null);
    }

    /**
     * Constructor
     *
     * @param name    column name
     * @param ldmType LDM type
     * @param title   title
     * @throws com.gooddata.exception.ModelException
     *          issue with a model consistency
     */
    public SourceColumn(String name, String ldmType, String title) {
        this(name, ldmType, title, null, null);
    }

    /**
     * Column name
     */
    /**
     * Column's name getter
     *
     * @return column name
     */
    public String getName() {
        return name;
    }

    /**
     * Column's name setter
     *
     * @param nm column's name
     */
    public void setName(String nm) {
        this.name = StringUtil.toIdentifier(nm);
    }

    /**
     * Column LDM type (ATTRIBUTE | LABEL | FACT)
     */
    /**
     * Column's LDM type getter
     *
     * @return column LDM type
     */
    public String getLdmType() {
        return ldmType;
    }

    /**
     * Column's LDM type setter
     *
     * @param ldmType column's LDM type (ATTRIBUTE | LABEL | FACT)
     */
    public void setLdmType(String ldmType) {
        this.ldmType = ldmType;
    }

    /**
     * LABEL's or REFERENCE's primary source column
     */
    /**
     * LABEL's or REFERENCE's primary source column getter.
     *
     * @return LABEL's or REFERENCE's primary source column, <tt>null</tt> for other LDM types regardless
     *         of earlier invocations of {@link #setReference(String)}
     */
    public String getReference() {
        if (SourceColumn.LDM_TYPE_REFERENCE.equals(getLdmType()) || SourceColumn.LDM_TYPE_LABEL.equals(getLdmType()) ||
                SourceColumn.LDM_TYPE_DATE.equals(getLdmType()) || SourceColumn.LDM_TYPE_HYPERLINK.equals(getLdmType())) {
            return reference;
        }
        return null;
    }

    /**
     * LABEL's or REFERENCE's primary source column setter
     *
     * @param reference LABEL's or REFERENCE's primary source column
     */
    public void setReference(String reference) {
        this.reference = StringUtil.toIdentifier(reference);
    }

    /**
     * REFERENCE's primary source schema
     */
    /**
     * LABEL's, DATE's or REFERENCE's primary source schema getter
     *
     * @return LABEL's, DATE's or REFERENCE's primary source schema. Returns <tt>null</tt> for other
     *         LDM types regardless of previous invocations of {@link #setSchemaReference(String)}
     */
    public String getSchemaReference() {
        if (LDM_TYPE_REFERENCE.equals(getLdmType()) || LDM_TYPE_DATE.equals(getLdmType()) || LDM_TYPE_LABEL.equals(getLdmType()) || LDM_TYPE_ATTRIBUTE.equals(getLdmType())) {
            return schemaReference;
        }
        return null;
    }

    /**
     * LABEL's or REFERENCE's primary source schema setter
     *
     * @param pks LABEL's or REFERENCE's primary source schema
     */
    public void setSchemaReference(String pks) {
        this.schemaReference = StringUtil.toIdentifier(pks);
    }

    /**
     * Column title
     */
    /**
     * Column's title getter
     *
     * @return column title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Column's title setter
     *
     * @param title column's title
     */
    public void setTitle(String title) {
        this.title = StringUtil.toTitle(title);
    }

    /**
     * Column's folder
     */
    /**
     * Column's folder getter
     *
     * @return column folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     * Column's folder setter
     *
     * @param folder column's folder
     */
    public void setFolder(String folder) {
        this.folder = StringUtil.toTitle(folder);
    }

    /**
     * Column's format
     */
    /**
     * Format getter
     *
     * @return column format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Format setter
     *
     * @param format column format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Column's datatype
     */
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSortLabel() {
        return sortLabel;
    }

    public void setSortLabel(String sortLabel) {
        this.sortLabel = sortLabel;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Include time
     */
    public String getDatetime() {
        return datetime;
    }

    public boolean isDatetime() {
        return (getDatetime() != null && ("true".equalsIgnoreCase(getDatetime()) || "1".equalsIgnoreCase(getDatetime())));
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return new StringBuffer(getName()).append("(").append(getLdmType()).append(")").toString();
    }

    /**
     * Validates the source column
     *
     * @throws com.gooddata.exception.ModelException
     *          in case of a validation error
     */
    public void validate() throws ModelException {
        /*
        if(StringUtil.containsInvvalidIdentifierChar(name))
            throw new ModelException("Column name "+name+" contains invalid characters");
         */
        if(getName() == null || getName().length() <=0)
            throw new ModelException("Column has no name.");
        if(getTitle() == null || getTitle().length() <=0)
            throw new ModelException("Column " + getName() + " has no title.");
        if (!LDM_TYPE_ATTRIBUTE.equals(getLdmType()) &&
                !LDM_TYPE_CONNECTION_POINT.equals(getLdmType()) &&
                !LDM_TYPE_DATE.equals(getLdmType()) &&
                !LDM_TYPE_FACT.equals(getLdmType()) &&
                !LDM_TYPE_IGNORE.equals(getLdmType()) &&
                !LDM_TYPE_LABEL.equals(getLdmType()) &&
                !LDM_TYPE_REFERENCE.equals(getLdmType()))
            throw new ModelException("Column " + getName() + " has invalid LDM type " + getLdmType());
        if (LDM_TYPE_LABEL.equals(getLdmType()) && (getReference() == null || getReference().length() <= 0))
            throw new ModelException("Column " + getName() + " has type LABEL but doesn't contain any reference.");
        if (LDM_TYPE_REFERENCE.equals(getLdmType()) && (getReference() == null || getReference().length() <= 0))
            throw new ModelException("Column " + getName() + " has type REFERENCE but doesn't contain any reference.");
        if (LDM_TYPE_REFERENCE.equals(getLdmType()) && (getSchemaReference() == null || getSchemaReference().length() <= 0))
            throw new ModelException("Column " + getName() + " has type REFERENCE but doesn't contain any schema reference.");
        if (LDM_TYPE_DATE.equals(getLdmType()) && (getFormat() == null || getFormat().length() <= 0))
            throw new ModelException("Column " + getName() + " has type DATE but doesn't contain any date format.");
        if (getSortLabel()!= null && (getSortOrder() == null || getSortOrder().length() <= 0))
            throw new ModelException("Column " + getName() + " has sort label but no sort order.");
        if (getSortOrder()!= null && !(getSortOrder().equals(LDM_SORT_ORDER_ASC) || getSortOrder().equals(LDM_SORT_ORDER_DESC)))
            throw new ModelException("Column " + getName() + " has invalid sort order '"+getSortOrder()+"'. Only 'ASC' or 'DESC' values are allowed.");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getLdmType() == null) ? 0 : getLdmType().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result
                + ((getReference() == null) ? 0 : getReference().hashCode());
        result = prime * result
                + ((getSchemaReference() == null) ? 0 : getSchemaReference().hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SourceColumn other = (SourceColumn) obj;
        if (getLdmType() == null) {
            if (other.getLdmType() != null)
                return false;
        } else if (!getLdmType().equals(other.getLdmType()))
            return false;
        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        if (getReference() == null) {
            if (other.getReference() != null)
                return false;
        } else if (!getReference().equals(other.getReference()))
            return false;
        if (getSchemaReference() == null) {
            if (other.getSchemaReference() != null)
                return false;
        } else if (!getSchemaReference().equals(other.getSchemaReference()))
            return false;

        return true;
    }
    
    /**
     * Returns true if both columns are any of LABEL or HYPERLINK and their other
     * properties are the same. Returns false otherwise
     */
    public boolean equalsToLabel(SourceColumn other) {
      if (!(LDM_TYPE_LABEL.equals(ldmType) || LDM_TYPE_HYPERLINK
            .equals(ldmType))) {
         return false;
      }
      if (!(LDM_TYPE_LABEL.equals(other.getLdmType()) || LDM_TYPE_HYPERLINK
            .equals(other.getLdmType()))) {
         return false;
      }
      if (getName() == null) {
         if (other.getName() != null)
            return false;
      } else if (!getName().equals(other.getName()))
         return false;
      if (getReference() == null) {
         if (other.getReference() != null)
            return false;
      } else if (!getReference().equals(other.getReference()))
         return false;
      return true;
    }
 
    /**
     * Transformation
     */
    public String getTransformation() {
        return transformation;
    }

    public void setTransformation(String transformation) {
        this.transformation = transformation;
    }

    public boolean isDateFact() {
        return isDateFact;
    }

    public void setDateFact(boolean dateFact) {
        isDateFact = dateFact;
    }

    public boolean isTimeFact() {
        return isTimeFact;
    }

    public void setTimeFact(boolean timeFact) {
        isTimeFact = timeFact;
    }
}
