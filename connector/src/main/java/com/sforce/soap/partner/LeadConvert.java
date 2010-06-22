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
 * LeadConvert.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class LeadConvert  implements java.io.Serializable {
    private String accountId;

    private String contactId;

    private String convertedStatus;

    private boolean doNotCreateOpportunity;

    private String leadId;

    private String opportunityName;

    private boolean overwriteLeadSource;

    private String ownerId;

    private boolean sendNotificationEmail;

    public LeadConvert() {
    }

    public LeadConvert(
           String accountId,
           String contactId,
           String convertedStatus,
           boolean doNotCreateOpportunity,
           String leadId,
           String opportunityName,
           boolean overwriteLeadSource,
           String ownerId,
           boolean sendNotificationEmail) {
           this.accountId = accountId;
           this.contactId = contactId;
           this.convertedStatus = convertedStatus;
           this.doNotCreateOpportunity = doNotCreateOpportunity;
           this.leadId = leadId;
           this.opportunityName = opportunityName;
           this.overwriteLeadSource = overwriteLeadSource;
           this.ownerId = ownerId;
           this.sendNotificationEmail = sendNotificationEmail;
    }


    /**
     * Gets the accountId value for this LeadConvert.
     * 
     * @return accountId
     */
    public String getAccountId() {
        return accountId;
    }


    /**
     * Sets the accountId value for this LeadConvert.
     * 
     * @param accountId
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }


    /**
     * Gets the contactId value for this LeadConvert.
     * 
     * @return contactId
     */
    public String getContactId() {
        return contactId;
    }


    /**
     * Sets the contactId value for this LeadConvert.
     * 
     * @param contactId
     */
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }


    /**
     * Gets the convertedStatus value for this LeadConvert.
     * 
     * @return convertedStatus
     */
    public String getConvertedStatus() {
        return convertedStatus;
    }


    /**
     * Sets the convertedStatus value for this LeadConvert.
     * 
     * @param convertedStatus
     */
    public void setConvertedStatus(String convertedStatus) {
        this.convertedStatus = convertedStatus;
    }


    /**
     * Gets the doNotCreateOpportunity value for this LeadConvert.
     * 
     * @return doNotCreateOpportunity
     */
    public boolean isDoNotCreateOpportunity() {
        return doNotCreateOpportunity;
    }


    /**
     * Sets the doNotCreateOpportunity value for this LeadConvert.
     * 
     * @param doNotCreateOpportunity
     */
    public void setDoNotCreateOpportunity(boolean doNotCreateOpportunity) {
        this.doNotCreateOpportunity = doNotCreateOpportunity;
    }


    /**
     * Gets the leadId value for this LeadConvert.
     * 
     * @return leadId
     */
    public String getLeadId() {
        return leadId;
    }


    /**
     * Sets the leadId value for this LeadConvert.
     * 
     * @param leadId
     */
    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }


    /**
     * Gets the opportunityName value for this LeadConvert.
     * 
     * @return opportunityName
     */
    public String getOpportunityName() {
        return opportunityName;
    }


    /**
     * Sets the opportunityName value for this LeadConvert.
     * 
     * @param opportunityName
     */
    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }


    /**
     * Gets the overwriteLeadSource value for this LeadConvert.
     * 
     * @return overwriteLeadSource
     */
    public boolean isOverwriteLeadSource() {
        return overwriteLeadSource;
    }


    /**
     * Sets the overwriteLeadSource value for this LeadConvert.
     * 
     * @param overwriteLeadSource
     */
    public void setOverwriteLeadSource(boolean overwriteLeadSource) {
        this.overwriteLeadSource = overwriteLeadSource;
    }


    /**
     * Gets the ownerId value for this LeadConvert.
     * 
     * @return ownerId
     */
    public String getOwnerId() {
        return ownerId;
    }


    /**
     * Sets the ownerId value for this LeadConvert.
     * 
     * @param ownerId
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }


    /**
     * Gets the sendNotificationEmail value for this LeadConvert.
     * 
     * @return sendNotificationEmail
     */
    public boolean isSendNotificationEmail() {
        return sendNotificationEmail;
    }


    /**
     * Sets the sendNotificationEmail value for this LeadConvert.
     * 
     * @param sendNotificationEmail
     */
    public void setSendNotificationEmail(boolean sendNotificationEmail) {
        this.sendNotificationEmail = sendNotificationEmail;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof LeadConvert)) return false;
        LeadConvert other = (LeadConvert) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.accountId==null && other.getAccountId()==null) || 
             (this.accountId!=null &&
              this.accountId.equals(other.getAccountId()))) &&
            ((this.contactId==null && other.getContactId()==null) || 
             (this.contactId!=null &&
              this.contactId.equals(other.getContactId()))) &&
            ((this.convertedStatus==null && other.getConvertedStatus()==null) || 
             (this.convertedStatus!=null &&
              this.convertedStatus.equals(other.getConvertedStatus()))) &&
            this.doNotCreateOpportunity == other.isDoNotCreateOpportunity() &&
            ((this.leadId==null && other.getLeadId()==null) || 
             (this.leadId!=null &&
              this.leadId.equals(other.getLeadId()))) &&
            ((this.opportunityName==null && other.getOpportunityName()==null) || 
             (this.opportunityName!=null &&
              this.opportunityName.equals(other.getOpportunityName()))) &&
            this.overwriteLeadSource == other.isOverwriteLeadSource() &&
            ((this.ownerId==null && other.getOwnerId()==null) || 
             (this.ownerId!=null &&
              this.ownerId.equals(other.getOwnerId()))) &&
            this.sendNotificationEmail == other.isSendNotificationEmail();
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
        if (getAccountId() != null) {
            _hashCode += getAccountId().hashCode();
        }
        if (getContactId() != null) {
            _hashCode += getContactId().hashCode();
        }
        if (getConvertedStatus() != null) {
            _hashCode += getConvertedStatus().hashCode();
        }
        _hashCode += (isDoNotCreateOpportunity() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLeadId() != null) {
            _hashCode += getLeadId().hashCode();
        }
        if (getOpportunityName() != null) {
            _hashCode += getOpportunityName().hashCode();
        }
        _hashCode += (isOverwriteLeadSource() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOwnerId() != null) {
            _hashCode += getOwnerId().hashCode();
        }
        _hashCode += (isSendNotificationEmail() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LeadConvert.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "LeadConvert"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "accountId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contactId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "contactId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("convertedStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertedStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("doNotCreateOpportunity");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "doNotCreateOpportunity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("leadId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "leadId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opportunityName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "opportunityName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("overwriteLeadSource");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "overwriteLeadSource"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ownerId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ownerId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sendNotificationEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sendNotificationEmail"));
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
