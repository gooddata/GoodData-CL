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
 * DescribeLayoutComponent.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class DescribeLayoutComponent  implements java.io.Serializable {
    private int displayLines;

    private int tabOrder;

    private com.sforce.soap.partner.LayoutComponentType type;

    private String value;

    public DescribeLayoutComponent() {
    }

    public DescribeLayoutComponent(
           int displayLines,
           int tabOrder,
           com.sforce.soap.partner.LayoutComponentType type,
           String value) {
           this.displayLines = displayLines;
           this.tabOrder = tabOrder;
           this.type = type;
           this.value = value;
    }


    /**
     * Gets the displayLines value for this DescribeLayoutComponent.
     * 
     * @return displayLines
     */
    public int getDisplayLines() {
        return displayLines;
    }


    /**
     * Sets the displayLines value for this DescribeLayoutComponent.
     * 
     * @param displayLines
     */
    public void setDisplayLines(int displayLines) {
        this.displayLines = displayLines;
    }


    /**
     * Gets the tabOrder value for this DescribeLayoutComponent.
     * 
     * @return tabOrder
     */
    public int getTabOrder() {
        return tabOrder;
    }


    /**
     * Sets the tabOrder value for this DescribeLayoutComponent.
     * 
     * @param tabOrder
     */
    public void setTabOrder(int tabOrder) {
        this.tabOrder = tabOrder;
    }


    /**
     * Gets the type value for this DescribeLayoutComponent.
     * 
     * @return type
     */
    public com.sforce.soap.partner.LayoutComponentType getType() {
        return type;
    }


    /**
     * Sets the type value for this DescribeLayoutComponent.
     * 
     * @param type
     */
    public void setType(com.sforce.soap.partner.LayoutComponentType type) {
        this.type = type;
    }


    /**
     * Gets the value value for this DescribeLayoutComponent.
     * 
     * @return value
     */
    public String getValue() {
        return value;
    }


    /**
     * Sets the value value for this DescribeLayoutComponent.
     * 
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof DescribeLayoutComponent)) return false;
        DescribeLayoutComponent other = (DescribeLayoutComponent) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.displayLines == other.getDisplayLines() &&
            this.tabOrder == other.getTabOrder() &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue())));
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
        _hashCode += getDisplayLines();
        _hashCode += getTabOrder();
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DescribeLayoutComponent.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutComponent"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayLines");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "displayLines"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tabOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "tabOrder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "layoutComponentType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
