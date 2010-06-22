/*
 * .
 */

/**
 * ProcessWorkitemRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class ProcessWorkitemRequest  extends ProcessRequest  implements java.io.Serializable {
    private String action;

    private String workitemId;

    public ProcessWorkitemRequest() {
    }

    public ProcessWorkitemRequest(
           String comments,
           String[] nextApproverIds,
           String action,
           String workitemId) {
        super(
            comments,
            nextApproverIds);
        this.action = action;
        this.workitemId = workitemId;
    }


    /**
     * Gets the action value for this ProcessWorkitemRequest.
     * 
     * @return action
     */
    public String getAction() {
        return action;
    }


    /**
     * Sets the action value for this ProcessWorkitemRequest.
     * 
     * @param action
     */
    public void setAction(String action) {
        this.action = action;
    }


    /**
     * Gets the workitemId value for this ProcessWorkitemRequest.
     * 
     * @return workitemId
     */
    public String getWorkitemId() {
        return workitemId;
    }


    /**
     * Sets the workitemId value for this ProcessWorkitemRequest.
     * 
     * @param workitemId
     */
    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ProcessWorkitemRequest)) return false;
        ProcessWorkitemRequest other = (ProcessWorkitemRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.action==null && other.getAction()==null) || 
             (this.action!=null &&
              this.action.equals(other.getAction()))) &&
            ((this.workitemId==null && other.getWorkitemId()==null) || 
             (this.workitemId!=null &&
              this.workitemId.equals(other.getWorkitemId())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getAction() != null) {
            _hashCode += getAction().hashCode();
        }
        if (getWorkitemId() != null) {
            _hashCode += getWorkitemId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProcessWorkitemRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ProcessWorkitemRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("action");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "action"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workitemId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "workitemId"));
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
