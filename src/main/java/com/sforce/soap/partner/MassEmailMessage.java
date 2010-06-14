/**
 * MassEmailMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class MassEmailMessage  extends Email  implements java.io.Serializable {
    private String description;

    private String[] targetObjectIds;

    private String templateId;

    private String[] whatIds;

    public MassEmailMessage() {
    }

    public MassEmailMessage(
           Boolean bccSender,
           EmailPriority emailPriority,
           String replyTo,
           Boolean saveAsActivity,
           String senderDisplayName,
           String subject,
           Boolean useSignature,
           String description,
           String[] targetObjectIds,
           String templateId,
           String[] whatIds) {
        super(
            bccSender,
            emailPriority,
            replyTo,
            saveAsActivity,
            senderDisplayName,
            subject,
            useSignature);
        this.description = description;
        this.targetObjectIds = targetObjectIds;
        this.templateId = templateId;
        this.whatIds = whatIds;
    }


    /**
     * Gets the description value for this MassEmailMessage.
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this MassEmailMessage.
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Gets the targetObjectIds value for this MassEmailMessage.
     * 
     * @return targetObjectIds
     */
    public String[] getTargetObjectIds() {
        return targetObjectIds;
    }


    /**
     * Sets the targetObjectIds value for this MassEmailMessage.
     * 
     * @param targetObjectIds
     */
    public void setTargetObjectIds(String[] targetObjectIds) {
        this.targetObjectIds = targetObjectIds;
    }

    public String getTargetObjectIds(int i) {
        return this.targetObjectIds[i];
    }

    public void setTargetObjectIds(int i, String _value) {
        this.targetObjectIds[i] = _value;
    }


    /**
     * Gets the templateId value for this MassEmailMessage.
     * 
     * @return templateId
     */
    public String getTemplateId() {
        return templateId;
    }


    /**
     * Sets the templateId value for this MassEmailMessage.
     * 
     * @param templateId
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }


    /**
     * Gets the whatIds value for this MassEmailMessage.
     * 
     * @return whatIds
     */
    public String[] getWhatIds() {
        return whatIds;
    }


    /**
     * Sets the whatIds value for this MassEmailMessage.
     * 
     * @param whatIds
     */
    public void setWhatIds(String[] whatIds) {
        this.whatIds = whatIds;
    }

    public String getWhatIds(int i) {
        return this.whatIds[i];
    }

    public void setWhatIds(int i, String _value) {
        this.whatIds[i] = _value;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof MassEmailMessage)) return false;
        MassEmailMessage other = (MassEmailMessage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.targetObjectIds==null && other.getTargetObjectIds()==null) || 
             (this.targetObjectIds!=null &&
              java.util.Arrays.equals(this.targetObjectIds, other.getTargetObjectIds()))) &&
            ((this.templateId==null && other.getTemplateId()==null) || 
             (this.templateId!=null &&
              this.templateId.equals(other.getTemplateId()))) &&
            ((this.whatIds==null && other.getWhatIds()==null) || 
             (this.whatIds!=null &&
              java.util.Arrays.equals(this.whatIds, other.getWhatIds())));
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
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getTargetObjectIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTargetObjectIds());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getTargetObjectIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTemplateId() != null) {
            _hashCode += getTemplateId().hashCode();
        }
        if (getWhatIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWhatIds());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getWhatIds(), i);
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
        new org.apache.axis.description.TypeDesc(MassEmailMessage.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "MassEmailMessage"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetObjectIds");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "targetObjectIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("templateId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "templateId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("whatIds");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "whatIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"));
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
