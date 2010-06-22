/*
 * .
 */

/**
 * MergeResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class MergeResult  implements java.io.Serializable {
    private Error[] errors;

    private String id;

    private String[] mergedRecordIds;

    private boolean success;

    private String[] updatedRelatedIds;

    public MergeResult() {
    }

    public MergeResult(
           Error[] errors,
           String id,
           String[] mergedRecordIds,
           boolean success,
           String[] updatedRelatedIds) {
           this.errors = errors;
           this.id = id;
           this.mergedRecordIds = mergedRecordIds;
           this.success = success;
           this.updatedRelatedIds = updatedRelatedIds;
    }


    /**
     * Gets the errors value for this MergeResult.
     * 
     * @return errors
     */
    public Error[] getErrors() {
        return errors;
    }


    /**
     * Sets the errors value for this MergeResult.
     * 
     * @param errors
     */
    public void setErrors(Error[] errors) {
        this.errors = errors;
    }

    public Error getErrors(int i) {
        return this.errors[i];
    }

    public void setErrors(int i, Error _value) {
        this.errors[i] = _value;
    }


    /**
     * Gets the id value for this MergeResult.
     * 
     * @return id
     */
    public String getId() {
        return id;
    }


    /**
     * Sets the id value for this MergeResult.
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Gets the mergedRecordIds value for this MergeResult.
     * 
     * @return mergedRecordIds
     */
    public String[] getMergedRecordIds() {
        return mergedRecordIds;
    }


    /**
     * Sets the mergedRecordIds value for this MergeResult.
     * 
     * @param mergedRecordIds
     */
    public void setMergedRecordIds(String[] mergedRecordIds) {
        this.mergedRecordIds = mergedRecordIds;
    }

    public String getMergedRecordIds(int i) {
        return this.mergedRecordIds[i];
    }

    public void setMergedRecordIds(int i, String _value) {
        this.mergedRecordIds[i] = _value;
    }


    /**
     * Gets the success value for this MergeResult.
     * 
     * @return success
     */
    public boolean isSuccess() {
        return success;
    }


    /**
     * Sets the success value for this MergeResult.
     * 
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }


    /**
     * Gets the updatedRelatedIds value for this MergeResult.
     * 
     * @return updatedRelatedIds
     */
    public String[] getUpdatedRelatedIds() {
        return updatedRelatedIds;
    }


    /**
     * Sets the updatedRelatedIds value for this MergeResult.
     * 
     * @param updatedRelatedIds
     */
    public void setUpdatedRelatedIds(String[] updatedRelatedIds) {
        this.updatedRelatedIds = updatedRelatedIds;
    }

    public String getUpdatedRelatedIds(int i) {
        return this.updatedRelatedIds[i];
    }

    public void setUpdatedRelatedIds(int i, String _value) {
        this.updatedRelatedIds[i] = _value;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof MergeResult)) return false;
        MergeResult other = (MergeResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.errors==null && other.getErrors()==null) || 
             (this.errors!=null &&
              java.util.Arrays.equals(this.errors, other.getErrors()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.mergedRecordIds==null && other.getMergedRecordIds()==null) || 
             (this.mergedRecordIds!=null &&
              java.util.Arrays.equals(this.mergedRecordIds, other.getMergedRecordIds()))) &&
            this.success == other.isSuccess() &&
            ((this.updatedRelatedIds==null && other.getUpdatedRelatedIds()==null) || 
             (this.updatedRelatedIds!=null &&
              java.util.Arrays.equals(this.updatedRelatedIds, other.getUpdatedRelatedIds())));
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
        if (getErrors() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getErrors());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getErrors(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getMergedRecordIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMergedRecordIds());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getMergedRecordIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isSuccess() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getUpdatedRelatedIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUpdatedRelatedIds());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getUpdatedRelatedIds(), i);
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
        new org.apache.axis.description.TypeDesc(MergeResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "MergeResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errors");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "errors"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "Error"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mergedRecordIds");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "mergedRecordIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("success");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "success"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updatedRelatedIds");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "updatedRelatedIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"));
        elemField.setMinOccurs(0);
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
