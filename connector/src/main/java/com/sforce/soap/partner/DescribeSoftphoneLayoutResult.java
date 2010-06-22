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
 * DescribeSoftphoneLayoutResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class DescribeSoftphoneLayoutResult  implements java.io.Serializable {
    private DescribeSoftphoneLayoutCallType[] callTypes;

    private String id;

    private String name;

    public DescribeSoftphoneLayoutResult() {
    }

    public DescribeSoftphoneLayoutResult(
           DescribeSoftphoneLayoutCallType[] callTypes,
           String id,
           String name) {
           this.callTypes = callTypes;
           this.id = id;
           this.name = name;
    }


    /**
     * Gets the callTypes value for this DescribeSoftphoneLayoutResult.
     * 
     * @return callTypes
     */
    public DescribeSoftphoneLayoutCallType[] getCallTypes() {
        return callTypes;
    }


    /**
     * Sets the callTypes value for this DescribeSoftphoneLayoutResult.
     * 
     * @param callTypes
     */
    public void setCallTypes(DescribeSoftphoneLayoutCallType[] callTypes) {
        this.callTypes = callTypes;
    }

    public DescribeSoftphoneLayoutCallType getCallTypes(int i) {
        return this.callTypes[i];
    }

    public void setCallTypes(int i, DescribeSoftphoneLayoutCallType _value) {
        this.callTypes[i] = _value;
    }


    /**
     * Gets the id value for this DescribeSoftphoneLayoutResult.
     * 
     * @return id
     */
    public String getId() {
        return id;
    }


    /**
     * Sets the id value for this DescribeSoftphoneLayoutResult.
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Gets the name value for this DescribeSoftphoneLayoutResult.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the name value for this DescribeSoftphoneLayoutResult.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof DescribeSoftphoneLayoutResult)) return false;
        DescribeSoftphoneLayoutResult other = (DescribeSoftphoneLayoutResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.callTypes==null && other.getCallTypes()==null) || 
             (this.callTypes!=null &&
              java.util.Arrays.equals(this.callTypes, other.getCallTypes()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName())));
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
        if (getCallTypes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCallTypes());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getCallTypes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DescribeSoftphoneLayoutResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSoftphoneLayoutResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("callTypes");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "callTypes"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSoftphoneLayoutCallType"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name"));
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
