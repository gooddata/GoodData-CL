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

/**
 * DescribeLayoutSection.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class DescribeLayoutSection  implements java.io.Serializable {
    private int columns;

    private String heading;

    private DescribeLayoutRow[] layoutRows;

    private int rows;

    private boolean useCollapsibleSection;

    private boolean useHeading;

    public DescribeLayoutSection() {
    }

    public DescribeLayoutSection(
           int columns,
           String heading,
           DescribeLayoutRow[] layoutRows,
           int rows,
           boolean useCollapsibleSection,
           boolean useHeading) {
           this.columns = columns;
           this.heading = heading;
           this.layoutRows = layoutRows;
           this.rows = rows;
           this.useCollapsibleSection = useCollapsibleSection;
           this.useHeading = useHeading;
    }


    /**
     * Gets the columns value for this DescribeLayoutSection.
     * 
     * @return columns
     */
    public int getColumns() {
        return columns;
    }


    /**
     * Sets the columns value for this DescribeLayoutSection.
     * 
     * @param columns
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }


    /**
     * Gets the heading value for this DescribeLayoutSection.
     * 
     * @return heading
     */
    public String getHeading() {
        return heading;
    }


    /**
     * Sets the heading value for this DescribeLayoutSection.
     * 
     * @param heading
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }


    /**
     * Gets the layoutRows value for this DescribeLayoutSection.
     * 
     * @return layoutRows
     */
    public DescribeLayoutRow[] getLayoutRows() {
        return layoutRows;
    }


    /**
     * Sets the layoutRows value for this DescribeLayoutSection.
     * 
     * @param layoutRows
     */
    public void setLayoutRows(DescribeLayoutRow[] layoutRows) {
        this.layoutRows = layoutRows;
    }

    public DescribeLayoutRow getLayoutRows(int i) {
        return this.layoutRows[i];
    }

    public void setLayoutRows(int i, DescribeLayoutRow _value) {
        this.layoutRows[i] = _value;
    }


    /**
     * Gets the rows value for this DescribeLayoutSection.
     * 
     * @return rows
     */
    public int getRows() {
        return rows;
    }


    /**
     * Sets the rows value for this DescribeLayoutSection.
     * 
     * @param rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }


    /**
     * Gets the useCollapsibleSection value for this DescribeLayoutSection.
     * 
     * @return useCollapsibleSection
     */
    public boolean isUseCollapsibleSection() {
        return useCollapsibleSection;
    }


    /**
     * Sets the useCollapsibleSection value for this DescribeLayoutSection.
     * 
     * @param useCollapsibleSection
     */
    public void setUseCollapsibleSection(boolean useCollapsibleSection) {
        this.useCollapsibleSection = useCollapsibleSection;
    }


    /**
     * Gets the useHeading value for this DescribeLayoutSection.
     * 
     * @return useHeading
     */
    public boolean isUseHeading() {
        return useHeading;
    }


    /**
     * Sets the useHeading value for this DescribeLayoutSection.
     * 
     * @param useHeading
     */
    public void setUseHeading(boolean useHeading) {
        this.useHeading = useHeading;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof DescribeLayoutSection)) return false;
        DescribeLayoutSection other = (DescribeLayoutSection) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.columns == other.getColumns() &&
            ((this.heading==null && other.getHeading()==null) || 
             (this.heading!=null &&
              this.heading.equals(other.getHeading()))) &&
            ((this.layoutRows==null && other.getLayoutRows()==null) || 
             (this.layoutRows!=null &&
              java.util.Arrays.equals(this.layoutRows, other.getLayoutRows()))) &&
            this.rows == other.getRows() &&
            this.useCollapsibleSection == other.isUseCollapsibleSection() &&
            this.useHeading == other.isUseHeading();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getColumns();
        if (getHeading() != null) {
            _hashCode += getHeading().hashCode();
        }
        if (getLayoutRows() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLayoutRows());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getLayoutRows(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getRows();
        _hashCode += (isUseCollapsibleSection() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUseHeading() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DescribeLayoutSection.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutSection"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("columns");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "columns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("heading");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "heading"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("layoutRows");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "layoutRows"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutRow"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rows");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "rows"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useCollapsibleSection");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "useCollapsibleSection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useHeading");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "useHeading"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
