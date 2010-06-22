/*
 * .
 */

/**
 * ChildRelationship.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class ChildRelationship  implements java.io.Serializable {
    private boolean cascadeDelete;

    private String childSObject;

    private Boolean deprecated;

    private Boolean deprecatedAndHidden;

    private String field;

    private String relationshipName;

    public ChildRelationship() {
    }

    public ChildRelationship(
           boolean cascadeDelete,
           String childSObject,
           Boolean deprecated,
           Boolean deprecatedAndHidden,
           String field,
           String relationshipName) {
           this.cascadeDelete = cascadeDelete;
           this.childSObject = childSObject;
           this.deprecated = deprecated;
           this.deprecatedAndHidden = deprecatedAndHidden;
           this.field = field;
           this.relationshipName = relationshipName;
    }


    /**
     * Gets the cascadeDelete value for this ChildRelationship.
     * 
     * @return cascadeDelete
     */
    public boolean isCascadeDelete() {
        return cascadeDelete;
    }


    /**
     * Sets the cascadeDelete value for this ChildRelationship.
     * 
     * @param cascadeDelete
     */
    public void setCascadeDelete(boolean cascadeDelete) {
        this.cascadeDelete = cascadeDelete;
    }


    /**
     * Gets the childSObject value for this ChildRelationship.
     * 
     * @return childSObject
     */
    public String getChildSObject() {
        return childSObject;
    }


    /**
     * Sets the childSObject value for this ChildRelationship.
     * 
     * @param childSObject
     */
    public void setChildSObject(String childSObject) {
        this.childSObject = childSObject;
    }


    /**
     * Gets the deprecated value for this ChildRelationship.
     * 
     * @return deprecated
     */
    public Boolean getDeprecated() {
        return deprecated;
    }


    /**
     * Sets the deprecated value for this ChildRelationship.
     * 
     * @param deprecated
     */
    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }


    /**
     * Gets the deprecatedAndHidden value for this ChildRelationship.
     * 
     * @return deprecatedAndHidden
     */
    public Boolean getDeprecatedAndHidden() {
        return deprecatedAndHidden;
    }


    /**
     * Sets the deprecatedAndHidden value for this ChildRelationship.
     * 
     * @param deprecatedAndHidden
     */
    public void setDeprecatedAndHidden(Boolean deprecatedAndHidden) {
        this.deprecatedAndHidden = deprecatedAndHidden;
    }


    /**
     * Gets the field value for this ChildRelationship.
     * 
     * @return field
     */
    public String getField() {
        return field;
    }


    /**
     * Sets the field value for this ChildRelationship.
     * 
     * @param field
     */
    public void setField(String field) {
        this.field = field;
    }


    /**
     * Gets the relationshipName value for this ChildRelationship.
     * 
     * @return relationshipName
     */
    public String getRelationshipName() {
        return relationshipName;
    }


    /**
     * Sets the relationshipName value for this ChildRelationship.
     * 
     * @param relationshipName
     */
    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ChildRelationship)) return false;
        ChildRelationship other = (ChildRelationship) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.cascadeDelete == other.isCascadeDelete() &&
            ((this.childSObject==null && other.getChildSObject()==null) || 
             (this.childSObject!=null &&
              this.childSObject.equals(other.getChildSObject()))) &&
            ((this.deprecated==null && other.getDeprecated()==null) || 
             (this.deprecated!=null &&
              this.deprecated.equals(other.getDeprecated()))) &&
            ((this.deprecatedAndHidden==null && other.getDeprecatedAndHidden()==null) || 
             (this.deprecatedAndHidden!=null &&
              this.deprecatedAndHidden.equals(other.getDeprecatedAndHidden()))) &&
            ((this.field==null && other.getField()==null) || 
             (this.field!=null &&
              this.field.equals(other.getField()))) &&
            ((this.relationshipName==null && other.getRelationshipName()==null) || 
             (this.relationshipName!=null &&
              this.relationshipName.equals(other.getRelationshipName())));
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
        _hashCode += (isCascadeDelete() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getChildSObject() != null) {
            _hashCode += getChildSObject().hashCode();
        }
        if (getDeprecated() != null) {
            _hashCode += getDeprecated().hashCode();
        }
        if (getDeprecatedAndHidden() != null) {
            _hashCode += getDeprecatedAndHidden().hashCode();
        }
        if (getField() != null) {
            _hashCode += getField().hashCode();
        }
        if (getRelationshipName() != null) {
            _hashCode += getRelationshipName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ChildRelationship.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ChildRelationship"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cascadeDelete");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "cascadeDelete"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("childSObject");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "childSObject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deprecated");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deprecated"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deprecatedAndHidden");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "deprecatedAndHidden"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "field"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relationshipName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "relationshipName"));
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
