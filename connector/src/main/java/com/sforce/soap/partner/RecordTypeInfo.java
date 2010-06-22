/*
 * .
 */

/**
 * RecordTypeInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class RecordTypeInfo  implements java.io.Serializable {
    private boolean available;

    private boolean defaultRecordTypeMapping;

    private String name;

    private String recordTypeId;

    public RecordTypeInfo() {
    }

    public RecordTypeInfo(
           boolean available,
           boolean defaultRecordTypeMapping,
           String name,
           String recordTypeId) {
           this.available = available;
           this.defaultRecordTypeMapping = defaultRecordTypeMapping;
           this.name = name;
           this.recordTypeId = recordTypeId;
    }


    /**
     * Gets the available value for this RecordTypeInfo.
     * 
     * @return available
     */
    public boolean isAvailable() {
        return available;
    }


    /**
     * Sets the available value for this RecordTypeInfo.
     * 
     * @param available
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }


    /**
     * Gets the defaultRecordTypeMapping value for this RecordTypeInfo.
     * 
     * @return defaultRecordTypeMapping
     */
    public boolean isDefaultRecordTypeMapping() {
        return defaultRecordTypeMapping;
    }


    /**
     * Sets the defaultRecordTypeMapping value for this RecordTypeInfo.
     * 
     * @param defaultRecordTypeMapping
     */
    public void setDefaultRecordTypeMapping(boolean defaultRecordTypeMapping) {
        this.defaultRecordTypeMapping = defaultRecordTypeMapping;
    }


    /**
     * Gets the name value for this RecordTypeInfo.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the name value for this RecordTypeInfo.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the recordTypeId value for this RecordTypeInfo.
     * 
     * @return recordTypeId
     */
    public String getRecordTypeId() {
        return recordTypeId;
    }


    /**
     * Sets the recordTypeId value for this RecordTypeInfo.
     * 
     * @param recordTypeId
     */
    public void setRecordTypeId(String recordTypeId) {
        this.recordTypeId = recordTypeId;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof RecordTypeInfo)) return false;
        RecordTypeInfo other = (RecordTypeInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.available == other.isAvailable() &&
            this.defaultRecordTypeMapping == other.isDefaultRecordTypeMapping() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.recordTypeId==null && other.getRecordTypeId()==null) || 
             (this.recordTypeId!=null &&
              this.recordTypeId.equals(other.getRecordTypeId())));
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
        _hashCode += (isAvailable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDefaultRecordTypeMapping() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getRecordTypeId() != null) {
            _hashCode += getRecordTypeId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RecordTypeInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "RecordTypeInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("available");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "available"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("defaultRecordTypeMapping");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "defaultRecordTypeMapping"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordTypeId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeId"));
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
