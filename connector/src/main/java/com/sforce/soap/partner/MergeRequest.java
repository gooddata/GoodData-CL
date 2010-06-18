/**
 * MergeRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class MergeRequest  implements java.io.Serializable {
    private com.sforce.soap.partner.sobject.SObject masterRecord;

    private String[] recordToMergeIds;

    public MergeRequest() {
    }

    public MergeRequest(
           com.sforce.soap.partner.sobject.SObject masterRecord,
           String[] recordToMergeIds) {
           this.masterRecord = masterRecord;
           this.recordToMergeIds = recordToMergeIds;
    }


    /**
     * Gets the masterRecord value for this MergeRequest.
     * 
     * @return masterRecord
     */
    public com.sforce.soap.partner.sobject.SObject getMasterRecord() {
        return masterRecord;
    }


    /**
     * Sets the masterRecord value for this MergeRequest.
     * 
     * @param masterRecord
     */
    public void setMasterRecord(com.sforce.soap.partner.sobject.SObject masterRecord) {
        this.masterRecord = masterRecord;
    }


    /**
     * Gets the recordToMergeIds value for this MergeRequest.
     * 
     * @return recordToMergeIds
     */
    public String[] getRecordToMergeIds() {
        return recordToMergeIds;
    }


    /**
     * Sets the recordToMergeIds value for this MergeRequest.
     * 
     * @param recordToMergeIds
     */
    public void setRecordToMergeIds(String[] recordToMergeIds) {
        this.recordToMergeIds = recordToMergeIds;
    }

    public String getRecordToMergeIds(int i) {
        return this.recordToMergeIds[i];
    }

    public void setRecordToMergeIds(int i, String _value) {
        this.recordToMergeIds[i] = _value;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof MergeRequest)) return false;
        MergeRequest other = (MergeRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.masterRecord==null && other.getMasterRecord()==null) || 
             (this.masterRecord!=null &&
              this.masterRecord.equals(other.getMasterRecord()))) &&
            ((this.recordToMergeIds==null && other.getRecordToMergeIds()==null) || 
             (this.recordToMergeIds!=null &&
              java.util.Arrays.equals(this.recordToMergeIds, other.getRecordToMergeIds())));
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
        if (getMasterRecord() != null) {
            _hashCode += getMasterRecord().hashCode();
        }
        if (getRecordToMergeIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRecordToMergeIds());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getRecordToMergeIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MergeRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "MergeRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("masterRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "masterRecord"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:sobject.partner.soap.sforce.com", "sObject"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordToMergeIds");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordToMergeIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
