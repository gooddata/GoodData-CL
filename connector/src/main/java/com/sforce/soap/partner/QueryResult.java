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
 * QueryResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class QueryResult  implements java.io.Serializable {
    private boolean done;

    private String queryLocator;

    private com.sforce.soap.partner.sobject.SObject[] records;

    private int size;

    public QueryResult() {
    }

    public QueryResult(
           boolean done,
           String queryLocator,
           com.sforce.soap.partner.sobject.SObject[] records,
           int size) {
           this.done = done;
           this.queryLocator = queryLocator;
           this.records = records;
           this.size = size;
    }


    /**
     * Gets the done value for this QueryResult.
     * 
     * @return done
     */
    public boolean isDone() {
        return done;
    }


    /**
     * Sets the done value for this QueryResult.
     * 
     * @param done
     */
    public void setDone(boolean done) {
        this.done = done;
    }


    /**
     * Gets the queryLocator value for this QueryResult.
     * 
     * @return queryLocator
     */
    public String getQueryLocator() {
        return queryLocator;
    }


    /**
     * Sets the queryLocator value for this QueryResult.
     * 
     * @param queryLocator
     */
    public void setQueryLocator(String queryLocator) {
        this.queryLocator = queryLocator;
    }


    /**
     * Gets the records value for this QueryResult.
     * 
     * @return records
     */
    public com.sforce.soap.partner.sobject.SObject[] getRecords() {
        return records;
    }


    /**
     * Sets the records value for this QueryResult.
     * 
     * @param records
     */
    public void setRecords(com.sforce.soap.partner.sobject.SObject[] records) {
        this.records = records;
    }

    public com.sforce.soap.partner.sobject.SObject getRecords(int i) {
        return this.records[i];
    }

    public void setRecords(int i, com.sforce.soap.partner.sobject.SObject _value) {
        this.records[i] = _value;
    }


    /**
     * Gets the size value for this QueryResult.
     * 
     * @return size
     */
    public int getSize() {
        return size;
    }


    /**
     * Sets the size value for this QueryResult.
     * 
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof QueryResult)) return false;
        QueryResult other = (QueryResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.done == other.isDone() &&
            ((this.queryLocator==null && other.getQueryLocator()==null) || 
             (this.queryLocator!=null &&
              this.queryLocator.equals(other.getQueryLocator()))) &&
            ((this.records==null && other.getRecords()==null) || 
             (this.records!=null &&
              java.util.Arrays.equals(this.records, other.getRecords()))) &&
            this.size == other.getSize();
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
        _hashCode += (isDone() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getQueryLocator() != null) {
            _hashCode += getQueryLocator().hashCode();
        }
        if (getRecords() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRecords());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getRecords(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getSize();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "QueryResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("done");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "done"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryLocator");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryLocator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("records");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "records"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:sobject.partner.soap.sforce.com", "sObject"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
