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
 * SendEmailError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class SendEmailError  implements java.io.Serializable {
    private String[] fields;

    private String message;

    private com.sforce.soap.partner.StatusCode statusCode;

    private String targetObjectId;

    public SendEmailError() {
    }

    public SendEmailError(
           String[] fields,
           String message,
           com.sforce.soap.partner.StatusCode statusCode,
           String targetObjectId) {
           this.fields = fields;
           this.message = message;
           this.statusCode = statusCode;
           this.targetObjectId = targetObjectId;
    }


    /**
     * Gets the fields value for this SendEmailError.
     * 
     * @return fields
     */
    public String[] getFields() {
        return fields;
    }


    /**
     * Sets the fields value for this SendEmailError.
     * 
     * @param fields
     */
    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String getFields(int i) {
        return this.fields[i];
    }

    public void setFields(int i, String _value) {
        this.fields[i] = _value;
    }


    /**
     * Gets the message value for this SendEmailError.
     * 
     * @return message
     */
    public String getMessage() {
        return message;
    }


    /**
     * Sets the message value for this SendEmailError.
     * 
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * Gets the statusCode value for this SendEmailError.
     * 
     * @return statusCode
     */
    public com.sforce.soap.partner.StatusCode getStatusCode() {
        return statusCode;
    }


    /**
     * Sets the statusCode value for this SendEmailError.
     * 
     * @param statusCode
     */
    public void setStatusCode(com.sforce.soap.partner.StatusCode statusCode) {
        this.statusCode = statusCode;
    }


    /**
     * Gets the targetObjectId value for this SendEmailError.
     * 
     * @return targetObjectId
     */
    public String getTargetObjectId() {
        return targetObjectId;
    }


    /**
     * Sets the targetObjectId value for this SendEmailError.
     * 
     * @param targetObjectId
     */
    public void setTargetObjectId(String targetObjectId) {
        this.targetObjectId = targetObjectId;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof SendEmailError)) return false;
        SendEmailError other = (SendEmailError) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fields==null && other.getFields()==null) || 
             (this.fields!=null &&
              java.util.Arrays.equals(this.fields, other.getFields()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.statusCode==null && other.getStatusCode()==null) || 
             (this.statusCode!=null &&
              this.statusCode.equals(other.getStatusCode()))) &&
            ((this.targetObjectId==null && other.getTargetObjectId()==null) || 
             (this.targetObjectId!=null &&
              this.targetObjectId.equals(other.getTargetObjectId())));
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
        if (getFields() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFields());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getFields(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getStatusCode() != null) {
            _hashCode += getStatusCode().hashCode();
        }
        if (getTargetObjectId() != null) {
            _hashCode += getTargetObjectId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SendEmailError.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SendEmailError"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fields");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "statusCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "StatusCode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetObjectId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "targetObjectId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
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
