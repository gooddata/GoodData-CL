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
 * GetDeletedResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class GetDeletedResult  implements java.io.Serializable {
    private DeletedRecord[] deletedRecords;

    private java.util.Calendar earliestDateAvailable;

    private java.util.Calendar latestDateCovered;

    private String sforceReserved;

    public GetDeletedResult() {
    }

    public GetDeletedResult(
           DeletedRecord[] deletedRecords,
           java.util.Calendar earliestDateAvailable,
           java.util.Calendar latestDateCovered,
           String sforceReserved) {
           this.deletedRecords = deletedRecords;
           this.earliestDateAvailable = earliestDateAvailable;
           this.latestDateCovered = latestDateCovered;
           this.sforceReserved = sforceReserved;
    }


    /**
     * Gets the deletedRecords value for this GetDeletedResult.
     * 
     * @return deletedRecords
     */
    public DeletedRecord[] getDeletedRecords() {
        return deletedRecords;
    }


    /**
     * Sets the deletedRecords value for this GetDeletedResult.
     * 
     * @param deletedRecords
     */
    public void setDeletedRecords(DeletedRecord[] deletedRecords) {
        this.deletedRecords = deletedRecords;
    }

    public DeletedRecord getDeletedRecords(int i) {
        return this.deletedRecords[i];
    }

    public void setDeletedRecords(int i, DeletedRecord _value) {
        this.deletedRecords[i] = _value;
    }


    /**
     * Gets the earliestDateAvailable value for this GetDeletedResult.
     * 
     * @return earliestDateAvailable
     */
    public java.util.Calendar getEarliestDateAvailable() {
        return earliestDateAvailable;
    }


    /**
     * Sets the earliestDateAvailable value for this GetDeletedResult.
     * 
     * @param earliestDateAvailable
     */
    public void setEarliestDateAvailable(java.util.Calendar earliestDateAvailable) {
        this.earliestDateAvailable = earliestDateAvailable;
    }


    /**
     * Gets the latestDateCovered value for this GetDeletedResult.
     * 
     * @return latestDateCovered
     */
    public java.util.Calendar getLatestDateCovered() {
        return latestDateCovered;
    }


    /**
     * Sets the latestDateCovered value for this GetDeletedResult.
     * 
     * @param latestDateCovered
     */
    public void setLatestDateCovered(java.util.Calendar latestDateCovered) {
        this.latestDateCovered = latestDateCovered;
    }


    /**
     * Gets the sforceReserved value for this GetDeletedResult.
     * 
     * @return sforceReserved
     */
    public String getSforceReserved() {
        return sforceReserved;
    }


    /**
     * Sets the sforceReserved value for this GetDeletedResult.
     * 
     * @param sforceReserved
     */
    public void setSforceReserved(String sforceReserved) {
        this.sforceReserved = sforceReserved;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof GetDeletedResult)) return false;
        GetDeletedResult other = (GetDeletedResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.deletedRecords==null && other.getDeletedRecords()==null) || 
             (this.deletedRecords!=null &&
              java.util.Arrays.equals(this.deletedRecords, other.getDeletedRecords()))) &&
            ((this.earliestDateAvailable==null && other.getEarliestDateAvailable()==null) || 
             (this.earliestDateAvailable!=null &&
              this.earliestDateAvailable.equals(other.getEarliestDateAvailable()))) &&
            ((this.latestDateCovered==null && other.getLatestDateCovered()==null) || 
             (this.latestDateCovered!=null &&
              this.latestDateCovered.equals(other.getLatestDateCovered()))) &&
            ((this.sforceReserved==null && other.getSforceReserved()==null) || 
             (this.sforceReserved!=null &&
              this.sforceReserved.equals(other.getSforceReserved())));
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
        if (getDeletedRecords() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDeletedRecords());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getDeletedRecords(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEarliestDateAvailable() != null) {
            _hashCode += getEarliestDateAvailable().hashCode();
        }
        if (getLatestDateCovered() != null) {
            _hashCode += getLatestDateCovered().hashCode();
        }
        if (getSforceReserved() != null) {
            _hashCode += getSforceReserved().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetDeletedResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "GetDeletedResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deletedRecords");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deletedRecords"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DeletedRecord"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("earliestDateAvailable");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "earliestDateAvailable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("latestDateCovered");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "latestDateCovered"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sforceReserved");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sforceReserved"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
