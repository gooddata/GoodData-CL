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
 * GetUserInfoResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class GetUserInfoResult  implements java.io.Serializable {
    private boolean accessibilityMode;

    private String currencySymbol;

    private String orgDefaultCurrencyIsoCode;

    private boolean orgHasPersonAccounts;

    private String organizationId;

    private boolean organizationMultiCurrency;

    private String organizationName;

    private String profileId;

    private String roleId;

    private String userDefaultCurrencyIsoCode;

    private String userEmail;

    private String userFullName;

    private String userId;

    private String userLanguage;

    private String userLocale;

    private String userName;

    private String userTimeZone;

    private String userType;

    private String userUiSkin;

    public GetUserInfoResult() {
    }

    public GetUserInfoResult(
           boolean accessibilityMode,
           String currencySymbol,
           String orgDefaultCurrencyIsoCode,
           boolean orgHasPersonAccounts,
           String organizationId,
           boolean organizationMultiCurrency,
           String organizationName,
           String profileId,
           String roleId,
           String userDefaultCurrencyIsoCode,
           String userEmail,
           String userFullName,
           String userId,
           String userLanguage,
           String userLocale,
           String userName,
           String userTimeZone,
           String userType,
           String userUiSkin) {
           this.accessibilityMode = accessibilityMode;
           this.currencySymbol = currencySymbol;
           this.orgDefaultCurrencyIsoCode = orgDefaultCurrencyIsoCode;
           this.orgHasPersonAccounts = orgHasPersonAccounts;
           this.organizationId = organizationId;
           this.organizationMultiCurrency = organizationMultiCurrency;
           this.organizationName = organizationName;
           this.profileId = profileId;
           this.roleId = roleId;
           this.userDefaultCurrencyIsoCode = userDefaultCurrencyIsoCode;
           this.userEmail = userEmail;
           this.userFullName = userFullName;
           this.userId = userId;
           this.userLanguage = userLanguage;
           this.userLocale = userLocale;
           this.userName = userName;
           this.userTimeZone = userTimeZone;
           this.userType = userType;
           this.userUiSkin = userUiSkin;
    }


    /**
     * Gets the accessibilityMode value for this GetUserInfoResult.
     * 
     * @return accessibilityMode
     */
    public boolean isAccessibilityMode() {
        return accessibilityMode;
    }


    /**
     * Sets the accessibilityMode value for this GetUserInfoResult.
     * 
     * @param accessibilityMode
     */
    public void setAccessibilityMode(boolean accessibilityMode) {
        this.accessibilityMode = accessibilityMode;
    }


    /**
     * Gets the currencySymbol value for this GetUserInfoResult.
     * 
     * @return currencySymbol
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }


    /**
     * Sets the currencySymbol value for this GetUserInfoResult.
     * 
     * @param currencySymbol
     */
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }


    /**
     * Gets the orgDefaultCurrencyIsoCode value for this GetUserInfoResult.
     * 
     * @return orgDefaultCurrencyIsoCode
     */
    public String getOrgDefaultCurrencyIsoCode() {
        return orgDefaultCurrencyIsoCode;
    }


    /**
     * Sets the orgDefaultCurrencyIsoCode value for this GetUserInfoResult.
     * 
     * @param orgDefaultCurrencyIsoCode
     */
    public void setOrgDefaultCurrencyIsoCode(String orgDefaultCurrencyIsoCode) {
        this.orgDefaultCurrencyIsoCode = orgDefaultCurrencyIsoCode;
    }


    /**
     * Gets the orgHasPersonAccounts value for this GetUserInfoResult.
     * 
     * @return orgHasPersonAccounts
     */
    public boolean isOrgHasPersonAccounts() {
        return orgHasPersonAccounts;
    }


    /**
     * Sets the orgHasPersonAccounts value for this GetUserInfoResult.
     * 
     * @param orgHasPersonAccounts
     */
    public void setOrgHasPersonAccounts(boolean orgHasPersonAccounts) {
        this.orgHasPersonAccounts = orgHasPersonAccounts;
    }


    /**
     * Gets the organizationId value for this GetUserInfoResult.
     * 
     * @return organizationId
     */
    public String getOrganizationId() {
        return organizationId;
    }


    /**
     * Sets the organizationId value for this GetUserInfoResult.
     * 
     * @param organizationId
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }


    /**
     * Gets the organizationMultiCurrency value for this GetUserInfoResult.
     * 
     * @return organizationMultiCurrency
     */
    public boolean isOrganizationMultiCurrency() {
        return organizationMultiCurrency;
    }


    /**
     * Sets the organizationMultiCurrency value for this GetUserInfoResult.
     * 
     * @param organizationMultiCurrency
     */
    public void setOrganizationMultiCurrency(boolean organizationMultiCurrency) {
        this.organizationMultiCurrency = organizationMultiCurrency;
    }


    /**
     * Gets the organizationName value for this GetUserInfoResult.
     * 
     * @return organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }


    /**
     * Sets the organizationName value for this GetUserInfoResult.
     * 
     * @param organizationName
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }


    /**
     * Gets the profileId value for this GetUserInfoResult.
     * 
     * @return profileId
     */
    public String getProfileId() {
        return profileId;
    }


    /**
     * Sets the profileId value for this GetUserInfoResult.
     * 
     * @param profileId
     */
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }


    /**
     * Gets the roleId value for this GetUserInfoResult.
     * 
     * @return roleId
     */
    public String getRoleId() {
        return roleId;
    }


    /**
     * Sets the roleId value for this GetUserInfoResult.
     * 
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


    /**
     * Gets the userDefaultCurrencyIsoCode value for this GetUserInfoResult.
     * 
     * @return userDefaultCurrencyIsoCode
     */
    public String getUserDefaultCurrencyIsoCode() {
        return userDefaultCurrencyIsoCode;
    }


    /**
     * Sets the userDefaultCurrencyIsoCode value for this GetUserInfoResult.
     * 
     * @param userDefaultCurrencyIsoCode
     */
    public void setUserDefaultCurrencyIsoCode(String userDefaultCurrencyIsoCode) {
        this.userDefaultCurrencyIsoCode = userDefaultCurrencyIsoCode;
    }


    /**
     * Gets the userEmail value for this GetUserInfoResult.
     * 
     * @return userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }


    /**
     * Sets the userEmail value for this GetUserInfoResult.
     * 
     * @param userEmail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    /**
     * Gets the userFullName value for this GetUserInfoResult.
     * 
     * @return userFullName
     */
    public String getUserFullName() {
        return userFullName;
    }


    /**
     * Sets the userFullName value for this GetUserInfoResult.
     * 
     * @param userFullName
     */
    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }


    /**
     * Gets the userId value for this GetUserInfoResult.
     * 
     * @return userId
     */
    public String getUserId() {
        return userId;
    }


    /**
     * Sets the userId value for this GetUserInfoResult.
     * 
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }


    /**
     * Gets the userLanguage value for this GetUserInfoResult.
     * 
     * @return userLanguage
     */
    public String getUserLanguage() {
        return userLanguage;
    }


    /**
     * Sets the userLanguage value for this GetUserInfoResult.
     * 
     * @param userLanguage
     */
    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }


    /**
     * Gets the userLocale value for this GetUserInfoResult.
     * 
     * @return userLocale
     */
    public String getUserLocale() {
        return userLocale;
    }


    /**
     * Sets the userLocale value for this GetUserInfoResult.
     * 
     * @param userLocale
     */
    public void setUserLocale(String userLocale) {
        this.userLocale = userLocale;
    }


    /**
     * Gets the userName value for this GetUserInfoResult.
     * 
     * @return userName
     */
    public String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this GetUserInfoResult.
     * 
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * Gets the userTimeZone value for this GetUserInfoResult.
     * 
     * @return userTimeZone
     */
    public String getUserTimeZone() {
        return userTimeZone;
    }


    /**
     * Sets the userTimeZone value for this GetUserInfoResult.
     * 
     * @param userTimeZone
     */
    public void setUserTimeZone(String userTimeZone) {
        this.userTimeZone = userTimeZone;
    }


    /**
     * Gets the userType value for this GetUserInfoResult.
     * 
     * @return userType
     */
    public String getUserType() {
        return userType;
    }


    /**
     * Sets the userType value for this GetUserInfoResult.
     * 
     * @param userType
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }


    /**
     * Gets the userUiSkin value for this GetUserInfoResult.
     * 
     * @return userUiSkin
     */
    public String getUserUiSkin() {
        return userUiSkin;
    }


    /**
     * Sets the userUiSkin value for this GetUserInfoResult.
     * 
     * @param userUiSkin
     */
    public void setUserUiSkin(String userUiSkin) {
        this.userUiSkin = userUiSkin;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof GetUserInfoResult)) return false;
        GetUserInfoResult other = (GetUserInfoResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.accessibilityMode == other.isAccessibilityMode() &&
            ((this.currencySymbol==null && other.getCurrencySymbol()==null) || 
             (this.currencySymbol!=null &&
              this.currencySymbol.equals(other.getCurrencySymbol()))) &&
            ((this.orgDefaultCurrencyIsoCode==null && other.getOrgDefaultCurrencyIsoCode()==null) || 
             (this.orgDefaultCurrencyIsoCode!=null &&
              this.orgDefaultCurrencyIsoCode.equals(other.getOrgDefaultCurrencyIsoCode()))) &&
            this.orgHasPersonAccounts == other.isOrgHasPersonAccounts() &&
            ((this.organizationId==null && other.getOrganizationId()==null) || 
             (this.organizationId!=null &&
              this.organizationId.equals(other.getOrganizationId()))) &&
            this.organizationMultiCurrency == other.isOrganizationMultiCurrency() &&
            ((this.organizationName==null && other.getOrganizationName()==null) || 
             (this.organizationName!=null &&
              this.organizationName.equals(other.getOrganizationName()))) &&
            ((this.profileId==null && other.getProfileId()==null) || 
             (this.profileId!=null &&
              this.profileId.equals(other.getProfileId()))) &&
            ((this.roleId==null && other.getRoleId()==null) || 
             (this.roleId!=null &&
              this.roleId.equals(other.getRoleId()))) &&
            ((this.userDefaultCurrencyIsoCode==null && other.getUserDefaultCurrencyIsoCode()==null) || 
             (this.userDefaultCurrencyIsoCode!=null &&
              this.userDefaultCurrencyIsoCode.equals(other.getUserDefaultCurrencyIsoCode()))) &&
            ((this.userEmail==null && other.getUserEmail()==null) || 
             (this.userEmail!=null &&
              this.userEmail.equals(other.getUserEmail()))) &&
            ((this.userFullName==null && other.getUserFullName()==null) || 
             (this.userFullName!=null &&
              this.userFullName.equals(other.getUserFullName()))) &&
            ((this.userId==null && other.getUserId()==null) || 
             (this.userId!=null &&
              this.userId.equals(other.getUserId()))) &&
            ((this.userLanguage==null && other.getUserLanguage()==null) || 
             (this.userLanguage!=null &&
              this.userLanguage.equals(other.getUserLanguage()))) &&
            ((this.userLocale==null && other.getUserLocale()==null) || 
             (this.userLocale!=null &&
              this.userLocale.equals(other.getUserLocale()))) &&
            ((this.userName==null && other.getUserName()==null) || 
             (this.userName!=null &&
              this.userName.equals(other.getUserName()))) &&
            ((this.userTimeZone==null && other.getUserTimeZone()==null) || 
             (this.userTimeZone!=null &&
              this.userTimeZone.equals(other.getUserTimeZone()))) &&
            ((this.userType==null && other.getUserType()==null) || 
             (this.userType!=null &&
              this.userType.equals(other.getUserType()))) &&
            ((this.userUiSkin==null && other.getUserUiSkin()==null) || 
             (this.userUiSkin!=null &&
              this.userUiSkin.equals(other.getUserUiSkin())));
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
        _hashCode += (isAccessibilityMode() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getCurrencySymbol() != null) {
            _hashCode += getCurrencySymbol().hashCode();
        }
        if (getOrgDefaultCurrencyIsoCode() != null) {
            _hashCode += getOrgDefaultCurrencyIsoCode().hashCode();
        }
        _hashCode += (isOrgHasPersonAccounts() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOrganizationId() != null) {
            _hashCode += getOrganizationId().hashCode();
        }
        _hashCode += (isOrganizationMultiCurrency() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOrganizationName() != null) {
            _hashCode += getOrganizationName().hashCode();
        }
        if (getProfileId() != null) {
            _hashCode += getProfileId().hashCode();
        }
        if (getRoleId() != null) {
            _hashCode += getRoleId().hashCode();
        }
        if (getUserDefaultCurrencyIsoCode() != null) {
            _hashCode += getUserDefaultCurrencyIsoCode().hashCode();
        }
        if (getUserEmail() != null) {
            _hashCode += getUserEmail().hashCode();
        }
        if (getUserFullName() != null) {
            _hashCode += getUserFullName().hashCode();
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
        if (getUserLanguage() != null) {
            _hashCode += getUserLanguage().hashCode();
        }
        if (getUserLocale() != null) {
            _hashCode += getUserLocale().hashCode();
        }
        if (getUserName() != null) {
            _hashCode += getUserName().hashCode();
        }
        if (getUserTimeZone() != null) {
            _hashCode += getUserTimeZone().hashCode();
        }
        if (getUserType() != null) {
            _hashCode += getUserType().hashCode();
        }
        if (getUserUiSkin() != null) {
            _hashCode += getUserUiSkin().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetUserInfoResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "GetUserInfoResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accessibilityMode");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "accessibilityMode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currencySymbol");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "currencySymbol"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orgDefaultCurrencyIsoCode");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "orgDefaultCurrencyIsoCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orgHasPersonAccounts");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "orgHasPersonAccounts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organizationId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "organizationId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organizationMultiCurrency");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "organizationMultiCurrency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organizationName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "organizationName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("profileId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "profileId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roleId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "roleId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userDefaultCurrencyIsoCode");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userDefaultCurrencyIsoCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userFullName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userFullName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userLanguage");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userLanguage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userLocale");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userLocale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userTimeZone");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userTimeZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userType");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userUiSkin");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userUiSkin"));
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
