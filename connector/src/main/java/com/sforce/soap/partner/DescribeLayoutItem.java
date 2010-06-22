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
 * DescribeLayoutItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class DescribeLayoutItem  implements java.io.Serializable {
    private boolean editable;

    private String label;

    private DescribeLayoutComponent[] layoutComponents;

    private boolean placeholder;

    private boolean required;

    public DescribeLayoutItem() {
    }

    public DescribeLayoutItem(
           boolean editable,
           String label,
           DescribeLayoutComponent[] layoutComponents,
           boolean placeholder,
           boolean required) {
           this.editable = editable;
           this.label = label;
           this.layoutComponents = layoutComponents;
           this.placeholder = placeholder;
           this.required = required;
    }


    /**
     * Gets the editable value for this DescribeLayoutItem.
     * 
     * @return editable
     */
    public boolean isEditable() {
        return editable;
    }


    /**
     * Sets the editable value for this DescribeLayoutItem.
     * 
     * @param editable
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }


    /**
     * Gets the label value for this DescribeLayoutItem.
     * 
     * @return label
     */
    public String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this DescribeLayoutItem.
     * 
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }


    /**
     * Gets the layoutComponents value for this DescribeLayoutItem.
     * 
     * @return layoutComponents
     */
    public DescribeLayoutComponent[] getLayoutComponents() {
        return layoutComponents;
    }


    /**
     * Sets the layoutComponents value for this DescribeLayoutItem.
     * 
     * @param layoutComponents
     */
    public void setLayoutComponents(DescribeLayoutComponent[] layoutComponents) {
        this.layoutComponents = layoutComponents;
    }

    public DescribeLayoutComponent getLayoutComponents(int i) {
        return this.layoutComponents[i];
    }

    public void setLayoutComponents(int i, DescribeLayoutComponent _value) {
        this.layoutComponents[i] = _value;
    }


    /**
     * Gets the placeholder value for this DescribeLayoutItem.
     * 
     * @return placeholder
     */
    public boolean isPlaceholder() {
        return placeholder;
    }


    /**
     * Sets the placeholder value for this DescribeLayoutItem.
     * 
     * @param placeholder
     */
    public void setPlaceholder(boolean placeholder) {
        this.placeholder = placeholder;
    }


    /**
     * Gets the required value for this DescribeLayoutItem.
     * 
     * @return required
     */
    public boolean isRequired() {
        return required;
    }


    /**
     * Sets the required value for this DescribeLayoutItem.
     * 
     * @param required
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof DescribeLayoutItem)) return false;
        DescribeLayoutItem other = (DescribeLayoutItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.editable == other.isEditable() &&
            ((this.label==null && other.getLabel()==null) || 
             (this.label!=null &&
              this.label.equals(other.getLabel()))) &&
            ((this.layoutComponents==null && other.getLayoutComponents()==null) || 
             (this.layoutComponents!=null &&
              java.util.Arrays.equals(this.layoutComponents, other.getLayoutComponents()))) &&
            this.placeholder == other.isPlaceholder() &&
            this.required == other.isRequired();
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
        _hashCode += (isEditable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        if (getLayoutComponents() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLayoutComponents());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getLayoutComponents(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isPlaceholder() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRequired() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DescribeLayoutItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("editable");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "editable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("layoutComponents");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "layoutComponents"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutComponent"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("placeholder");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "placeholder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("required");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "required"));
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
