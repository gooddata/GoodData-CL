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
 * ProcessResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class ProcessResult  implements java.io.Serializable {
    private String[] actorIds;

    private String entityId;

    private Error[] errors;

    private String instanceId;

    private String instanceStatus;

    private String[] newWorkitemIds;

    private boolean success;

    public ProcessResult() {
    }

    public ProcessResult(
           String[] actorIds,
           String entityId,
           Error[] errors,
           String instanceId,
           String instanceStatus,
           String[] newWorkitemIds,
           boolean success) {
           this.actorIds = actorIds;
           this.entityId = entityId;
           this.errors = errors;
           this.instanceId = instanceId;
           this.instanceStatus = instanceStatus;
           this.newWorkitemIds = newWorkitemIds;
           this.success = success;
    }


    /**
     * Gets the actorIds value for this ProcessResult.
     * 
     * @return actorIds
     */
    public String[] getActorIds() {
        return actorIds;
    }


    /**
     * Sets the actorIds value for this ProcessResult.
     * 
     * @param actorIds
     */
    public void setActorIds(String[] actorIds) {
        this.actorIds = actorIds;
    }

    public String getActorIds(int i) {
        return this.actorIds[i];
    }

    public void setActorIds(int i, String _value) {
        this.actorIds[i] = _value;
    }


    /**
     * Gets the entityId value for this ProcessResult.
     * 
     * @return entityId
     */
    public String getEntityId() {
        return entityId;
    }


    /**
     * Sets the entityId value for this ProcessResult.
     * 
     * @param entityId
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }


    /**
     * Gets the errors value for this ProcessResult.
     * 
     * @return errors
     */
    public Error[] getErrors() {
        return errors;
    }


    /**
     * Sets the errors value for this ProcessResult.
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
     * Gets the instanceId value for this ProcessResult.
     * 
     * @return instanceId
     */
    public String getInstanceId() {
        return instanceId;
    }


    /**
     * Sets the instanceId value for this ProcessResult.
     * 
     * @param instanceId
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }


    /**
     * Gets the instanceStatus value for this ProcessResult.
     * 
     * @return instanceStatus
     */
    public String getInstanceStatus() {
        return instanceStatus;
    }


    /**
     * Sets the instanceStatus value for this ProcessResult.
     * 
     * @param instanceStatus
     */
    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
    }


    /**
     * Gets the newWorkitemIds value for this ProcessResult.
     * 
     * @return newWorkitemIds
     */
    public String[] getNewWorkitemIds() {
        return newWorkitemIds;
    }


    /**
     * Sets the newWorkitemIds value for this ProcessResult.
     * 
     * @param newWorkitemIds
     */
    public void setNewWorkitemIds(String[] newWorkitemIds) {
        this.newWorkitemIds = newWorkitemIds;
    }

    public String getNewWorkitemIds(int i) {
        return this.newWorkitemIds[i];
    }

    public void setNewWorkitemIds(int i, String _value) {
        this.newWorkitemIds[i] = _value;
    }


    /**
     * Gets the success value for this ProcessResult.
     * 
     * @return success
     */
    public boolean isSuccess() {
        return success;
    }


    /**
     * Sets the success value for this ProcessResult.
     * 
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof ProcessResult)) return false;
        ProcessResult other = (ProcessResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.actorIds==null && other.getActorIds()==null) || 
             (this.actorIds!=null &&
              java.util.Arrays.equals(this.actorIds, other.getActorIds()))) &&
            ((this.entityId==null && other.getEntityId()==null) || 
             (this.entityId!=null &&
              this.entityId.equals(other.getEntityId()))) &&
            ((this.errors==null && other.getErrors()==null) || 
             (this.errors!=null &&
              java.util.Arrays.equals(this.errors, other.getErrors()))) &&
            ((this.instanceId==null && other.getInstanceId()==null) || 
             (this.instanceId!=null &&
              this.instanceId.equals(other.getInstanceId()))) &&
            ((this.instanceStatus==null && other.getInstanceStatus()==null) || 
             (this.instanceStatus!=null &&
              this.instanceStatus.equals(other.getInstanceStatus()))) &&
            ((this.newWorkitemIds==null && other.getNewWorkitemIds()==null) || 
             (this.newWorkitemIds!=null &&
              java.util.Arrays.equals(this.newWorkitemIds, other.getNewWorkitemIds()))) &&
            this.success == other.isSuccess();
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
        if (getActorIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getActorIds());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getActorIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEntityId() != null) {
            _hashCode += getEntityId().hashCode();
        }
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
        if (getInstanceId() != null) {
            _hashCode += getInstanceId().hashCode();
        }
        if (getInstanceStatus() != null) {
            _hashCode += getInstanceStatus().hashCode();
        }
        if (getNewWorkitemIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNewWorkitemIds());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getNewWorkitemIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isSuccess() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProcessResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ProcessResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actorIds");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actorIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entityId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "entityId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errors");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "errors"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "Error"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instanceId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "instanceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instanceStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "instanceStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newWorkitemIds");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "newWorkitemIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("success");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "success"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
