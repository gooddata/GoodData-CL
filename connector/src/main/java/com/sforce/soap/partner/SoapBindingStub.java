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
 * SoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class SoapBindingStub extends org.apache.axis.client.Stub implements Soap {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[30];
        _initOperationDesc1();
        _initOperationDesc2();
        _initOperationDesc3();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("login");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "username"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "LoginResult"));
        oper.setReturnClass(LoginResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "LoginFault"),
                      "com.sforce.soap.partner.fault.LoginFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "LoginFault"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("describeSObject");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjectType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSObjectResult"));
        oper.setReturnClass(DescribeSObjectResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("describeSObjects");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjectType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSObjectResult"));
        oper.setReturnClass(DescribeSObjectResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("describeGlobal");
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeGlobalResult"));
        oper.setReturnClass(DescribeGlobalResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("describeLayout");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjectType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "recordTypeIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"), String[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutResult"));
        oper.setReturnClass(DescribeLayoutResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("describeSoftphoneLayout");
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSoftphoneLayoutResult"));
        oper.setReturnClass(DescribeSoftphoneLayoutResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("describeTabs");
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeTabSetResult"));
        oper.setReturnClass(DescribeTabSetResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("create");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjects"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:sobject.partner.soap.sforce.com", "sObject"), com.sforce.soap.partner.sobject.SObject[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SaveResult"));
        oper.setReturnClass(SaveResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"),
                      "com.sforce.soap.partner.fault.InvalidFieldFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("update");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjects"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:sobject.partner.soap.sforce.com", "sObject"), com.sforce.soap.partner.sobject.SObject[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SaveResult"));
        oper.setReturnClass(SaveResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"),
                      "com.sforce.soap.partner.fault.InvalidFieldFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("upsert");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "externalIDFieldName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjects"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:sobject.partner.soap.sforce.com", "sObject"), com.sforce.soap.partner.sobject.SObject[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "UpsertResult"));
        oper.setReturnClass(com.sforce.soap.partner.UpsertResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"),
                      "com.sforce.soap.partner.fault.InvalidFieldFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"), 
                      true
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("merge");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "MergeRequest"), MergeRequest[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "MergeResult"));
        oper.setReturnClass(MergeResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"),
                      "com.sforce.soap.partner.fault.InvalidFieldFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"), 
                      true
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("delete");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"), String[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DeleteResult"));
        oper.setReturnClass(DeleteResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("undelete");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"), String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "UndeleteResult"));
        oper.setReturnClass(com.sforce.soap.partner.UndeleteResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("emptyRecycleBin");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"), String[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "EmptyRecycleBinResult"));
        oper.setReturnClass(EmptyRecycleBinResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("retrieve");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fieldList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjectType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"), String[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:sobject.partner.soap.sforce.com", "sObject"));
        oper.setReturnClass(com.sforce.soap.partner.sobject.SObject[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "MalformedQueryFault"),
                      "com.sforce.soap.partner.fault.MalformedQueryFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "MalformedQueryFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"),
                      "com.sforce.soap.partner.fault.InvalidFieldFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"), 
                      true
                     ));
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("process");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ProcessRequest"), ProcessRequest[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ProcessResult"));
        oper.setReturnClass(ProcessResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("convertLead");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "leadConverts"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "LeadConvert"), LeadConvert[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "LeadConvertResult"));
        oper.setReturnClass(LeadConvertResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("logout");
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("invalidateSessions");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sessionIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "InvalidateSessionsResult"));
        oper.setReturnClass(InvalidateSessionsResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[18] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getDeleted");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjectType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "startDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "endDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "GetDeletedResult"));
        oper.setReturnClass(GetDeletedResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        _operations[19] = oper;

    }

    private static void _initOperationDesc3(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getUpdated");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjectType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "startDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "endDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "GetUpdatedResult"));
        oper.setReturnClass(GetUpdatedResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        _operations[20] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("query");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryString"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "QueryResult"));
        oper.setReturnClass(QueryResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidQueryLocatorFault"),
                      "com.sforce.soap.partner.fault.InvalidQueryLocatorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidQueryLocatorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "MalformedQueryFault"),
                      "com.sforce.soap.partner.fault.MalformedQueryFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "MalformedQueryFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"),
                      "com.sforce.soap.partner.fault.InvalidFieldFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"), 
                      true
                     ));
        _operations[21] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryAll");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryString"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "QueryResult"));
        oper.setReturnClass(QueryResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidQueryLocatorFault"),
                      "com.sforce.soap.partner.fault.InvalidQueryLocatorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidQueryLocatorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "MalformedQueryFault"),
                      "com.sforce.soap.partner.fault.MalformedQueryFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "MalformedQueryFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"),
                      "com.sforce.soap.partner.fault.InvalidFieldFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"), 
                      true
                     ));
        _operations[22] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryMore");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryLocator"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "QueryLocator"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "QueryResult"));
        oper.setReturnClass(QueryResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidQueryLocatorFault"),
                      "com.sforce.soap.partner.fault.InvalidQueryLocatorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidQueryLocatorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"),
                      "com.sforce.soap.partner.fault.InvalidFieldFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"), 
                      true
                     ));
        _operations[23] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("search");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "searchString"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SearchResult"));
        oper.setReturnClass(SearchResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "MalformedSearchFault"),
                      "com.sforce.soap.partner.fault.MalformedSearchFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "MalformedSearchFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"),
                      "com.sforce.soap.partner.fault.InvalidSObjectFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"),
                      "com.sforce.soap.partner.fault.InvalidFieldFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault"), 
                      true
                     ));
        _operations[24] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getServerTimestamp");
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "GetServerTimestampResult"));
        oper.setReturnClass(GetServerTimestampResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[25] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setPassword");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"), String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SetPasswordResult"));
        oper.setReturnClass(SetPasswordResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidNewPasswordFault"),
                      "com.sforce.soap.partner.fault.InvalidNewPasswordFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidNewPasswordFault"), 
                      true
                     ));
        _operations[26] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("resetPassword");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "userId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID"), String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ResetPasswordResult"));
        oper.setReturnClass(ResetPasswordResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"),
                      "com.sforce.soap.partner.fault.InvalidIdFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault"), 
                      true
                     ));
        _operations[27] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getUserInfo");
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "GetUserInfoResult"));
        oper.setReturnClass(GetUserInfoResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[28] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("sendEmail");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "messages"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "Email"), Email[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SendEmailResult"));
        oper.setReturnClass(SendEmailResult[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"),
                      "com.sforce.soap.partner.fault.UnexpectedErrorFault",
                      new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault"), 
                      true
                     ));
        _operations[29] = oper;

    }

    public SoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public SoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public SoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
        addBindings0();
        addBindings1();
    }

    private void addBindings0() {
            Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "ApiFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.ApiFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "ApiQueryFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.ApiQueryFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "ExceptionCode");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.ExceptionCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "FaultCode");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.FaultCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidFieldFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.InvalidFieldFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidIdFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.InvalidIdFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidNewPasswordFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.InvalidNewPasswordFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidQueryLocatorFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.InvalidQueryLocatorFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "InvalidSObjectFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.InvalidSObjectFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "LoginFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.LoginFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "MalformedQueryFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.MalformedQueryFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "MalformedSearchFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.MalformedSearchFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fault.partner.soap.sforce.com", "UnexpectedErrorFault");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.fault.UnexpectedErrorFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">AllowFieldTruncationHeader");
            cachedSerQNames.add(qName);
            cls = AllowFieldTruncationHeader.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">AssignmentRuleHeader");
            cachedSerQNames.add(qName);
            cls = AssignmentRuleHeader.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">CallOptions");
            cachedSerQNames.add(qName);
            cls = CallOptions.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">convertLead");
            cachedSerQNames.add(qName);
            cls = LeadConvert[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "LeadConvert");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "leadConverts");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">convertLeadResponse");
            cachedSerQNames.add(qName);
            cls = LeadConvertResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "LeadConvertResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">create");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.sobject.SObject[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:sobject.partner.soap.sforce.com", "sObject");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjects");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">createResponse");
            cachedSerQNames.add(qName);
            cls = SaveResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SaveResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">DebuggingHeader");
            cachedSerQNames.add(qName);
            cls = DebuggingHeader.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">DebuggingInfo");
            cachedSerQNames.add(qName);
            cls = DebuggingInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">delete");
            cachedSerQNames.add(qName);
            cls = String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">deleteResponse");
            cachedSerQNames.add(qName);
            cls = DeleteResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DeleteResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeGlobal");
            cachedSerQNames.add(qName);
            cls = DescribeGlobal.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeGlobalResponse");
            cachedSerQNames.add(qName);
            cls = DescribeGlobalResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeLayout");
            cachedSerQNames.add(qName);
            cls = DescribeLayoutType0.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeLayoutResponse");
            cachedSerQNames.add(qName);
            cls = DescribeLayoutResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeSObject");
            cachedSerQNames.add(qName);
            cls = DescribeSObject.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeSObjectResponse");
            cachedSerQNames.add(qName);
            cls = DescribeSObjectResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeSObjects");
            cachedSerQNames.add(qName);
            cls = String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjectType");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeSObjectsResponse");
            cachedSerQNames.add(qName);
            cls = DescribeSObjectResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSObjectResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeSoftphoneLayout");
            cachedSerQNames.add(qName);
            cls = DescribeSoftphoneLayout.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeSoftphoneLayoutResponse");
            cachedSerQNames.add(qName);
            cls = DescribeSoftphoneLayoutResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeTabs");
            cachedSerQNames.add(qName);
            cls = DescribeTabs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">describeTabsResponse");
            cachedSerQNames.add(qName);
            cls = DescribeTabSetResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeTabSetResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">EmailHeader");
            cachedSerQNames.add(qName);
            cls = EmailHeader.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">emptyRecycleBin");
            cachedSerQNames.add(qName);
            cls = String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">emptyRecycleBinResponse");
            cachedSerQNames.add(qName);
            cls = EmptyRecycleBinResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "EmptyRecycleBinResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">getDeleted");
            cachedSerQNames.add(qName);
            cls = GetDeleted.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">getDeletedResponse");
            cachedSerQNames.add(qName);
            cls = GetDeletedResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">getServerTimestamp");
            cachedSerQNames.add(qName);
            cls = GetServerTimestamp.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">getServerTimestampResponse");
            cachedSerQNames.add(qName);
            cls = GetServerTimestampResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">getUpdated");
            cachedSerQNames.add(qName);
            cls = GetUpdated.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">getUpdatedResponse");
            cachedSerQNames.add(qName);
            cls = GetUpdatedResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">getUserInfo");
            cachedSerQNames.add(qName);
            cls = GetUserInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">getUserInfoResponse");
            cachedSerQNames.add(qName);
            cls = GetUserInfoResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">invalidateSessions");
            cachedSerQNames.add(qName);
            cls = String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sessionIds");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">invalidateSessionsResponse");
            cachedSerQNames.add(qName);
            cls = InvalidateSessionsResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "InvalidateSessionsResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">LocaleOptions");
            cachedSerQNames.add(qName);
            cls = LocaleOptions.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">login");
            cachedSerQNames.add(qName);
            cls = Login.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">loginResponse");
            cachedSerQNames.add(qName);
            cls = LoginResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">LoginScopeHeader");
            cachedSerQNames.add(qName);
            cls = LoginScopeHeader.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">logout");
            cachedSerQNames.add(qName);
            cls = Logout.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">logoutResponse");
            cachedSerQNames.add(qName);
            cls = LogoutResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">merge");
            cachedSerQNames.add(qName);
            cls = MergeRequest[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "MergeRequest");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "request");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">mergeResponse");
            cachedSerQNames.add(qName);
            cls = MergeResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "MergeResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">MruHeader");
            cachedSerQNames.add(qName);
            cls = MruHeader.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">PackageVersionHeader");
            cachedSerQNames.add(qName);
            cls = PackageVersion[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "PackageVersion");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "packageVersions");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">process");
            cachedSerQNames.add(qName);
            cls = ProcessRequest[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ProcessRequest");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "actions");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">processResponse");
            cachedSerQNames.add(qName);
            cls = ProcessResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ProcessResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">query");
            cachedSerQNames.add(qName);
            cls = Query.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">queryAll");
            cachedSerQNames.add(qName);
            cls = QueryAll.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">queryAllResponse");
            cachedSerQNames.add(qName);
            cls = QueryAllResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">queryMore");
            cachedSerQNames.add(qName);
            cls = QueryMore.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">queryMoreResponse");
            cachedSerQNames.add(qName);
            cls = QueryMoreResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">QueryOptions");
            cachedSerQNames.add(qName);
            cls = QueryOptions.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">queryResponse");
            cachedSerQNames.add(qName);
            cls = QueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">resetPassword");
            cachedSerQNames.add(qName);
            cls = ResetPassword.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">resetPasswordResponse");
            cachedSerQNames.add(qName);
            cls = ResetPasswordResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">retrieve");
            cachedSerQNames.add(qName);
            cls = Retrieve.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">retrieveResponse");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.sobject.SObject[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:sobject.partner.soap.sforce.com", "sObject");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">search");
            cachedSerQNames.add(qName);
            cls = Search.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">searchResponse");
            cachedSerQNames.add(qName);
            cls = SearchResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">sendEmail");
            cachedSerQNames.add(qName);
            cls = Email[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "Email");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "messages");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">sendEmailResponse");
            cachedSerQNames.add(qName);
            cls = SendEmailResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SendEmailResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">SessionHeader");
            cachedSerQNames.add(qName);
            cls = SessionHeader.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">setPassword");
            cachedSerQNames.add(qName);
            cls = SetPassword.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">setPasswordResponse");
            cachedSerQNames.add(qName);
            cls = SetPasswordResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">undelete");
            cachedSerQNames.add(qName);
            cls = String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ids");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">undeleteResponse");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.UndeleteResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "UndeleteResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">update");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.sobject.SObject[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:sobject.partner.soap.sforce.com", "sObject");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sObjects");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">updateResponse");
            cachedSerQNames.add(qName);
            cls = SaveResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SaveResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">upsert");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.Upsert.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">upsertResponse");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.UpsertResult[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "UpsertResult");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "result");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", ">UserTerritoryDeleteHeader");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.UserTerritoryDeleteHeader.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ChildRelationship");
            cachedSerQNames.add(qName);
            cls = ChildRelationship.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DebugLevel");
            cachedSerQNames.add(qName);
            cls = DebugLevel.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DeletedRecord");
            cachedSerQNames.add(qName);
            cls = DeletedRecord.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DeleteResult");
            cachedSerQNames.add(qName);
            cls = DeleteResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeGlobalResult");
            cachedSerQNames.add(qName);
            cls = DescribeGlobalResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayout");
            cachedSerQNames.add(qName);
            cls = DescribeLayout.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutButton");
            cachedSerQNames.add(qName);
            cls = DescribeLayoutButton.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutButtonSection");
            cachedSerQNames.add(qName);
            cls = DescribeLayoutButton[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutButton");
            qName2 = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "detailButtons");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutComponent");
            cachedSerQNames.add(qName);
            cls = DescribeLayoutComponent.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutItem");
            cachedSerQNames.add(qName);
            cls = DescribeLayoutItem.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutResult");
            cachedSerQNames.add(qName);
            cls = DescribeLayoutResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutRow");
            cachedSerQNames.add(qName);
            cls = DescribeLayoutRow.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeLayoutSection");
            cachedSerQNames.add(qName);
            cls = DescribeLayoutSection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSObjectResult");
            cachedSerQNames.add(qName);
            cls = DescribeSObjectResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }
    private void addBindings1() {
            Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSoftphoneLayoutCallType");
            cachedSerQNames.add(qName);
            cls = DescribeSoftphoneLayoutCallType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSoftphoneLayoutInfoField");
            cachedSerQNames.add(qName);
            cls = DescribeSoftphoneLayoutInfoField.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSoftphoneLayoutItem");
            cachedSerQNames.add(qName);
            cls = DescribeSoftphoneLayoutItem.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSoftphoneLayoutResult");
            cachedSerQNames.add(qName);
            cls = DescribeSoftphoneLayoutResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeSoftphoneLayoutSection");
            cachedSerQNames.add(qName);
            cls = DescribeSoftphoneLayoutSection.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeTab");
            cachedSerQNames.add(qName);
            cls = DescribeTab.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "DescribeTabSetResult");
            cachedSerQNames.add(qName);
            cls = DescribeTabSetResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "Email");
            cachedSerQNames.add(qName);
            cls = Email.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "EmailFileAttachment");
            cachedSerQNames.add(qName);
            cls = EmailFileAttachment.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "EmailPriority");
            cachedSerQNames.add(qName);
            cls = EmailPriority.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "EmptyRecycleBinResult");
            cachedSerQNames.add(qName);
            cls = EmptyRecycleBinResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "Error");
            cachedSerQNames.add(qName);
            cls = Error.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "Field");
            cachedSerQNames.add(qName);
            cls = Field.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fieldType");
            cachedSerQNames.add(qName);
            cls = FieldType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "GetDeletedResult");
            cachedSerQNames.add(qName);
            cls = GetDeletedResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "GetServerTimestampResult");
            cachedSerQNames.add(qName);
            cls = GetServerTimestampResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "GetUpdatedResult");
            cachedSerQNames.add(qName);
            cls = GetUpdatedResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "GetUserInfoResult");
            cachedSerQNames.add(qName);
            cls = GetUserInfoResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ID");
            cachedSerQNames.add(qName);
            cls = String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "InvalidateSessionsResult");
            cachedSerQNames.add(qName);
            cls = InvalidateSessionsResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "layoutComponentType");
            cachedSerQNames.add(qName);
            cls = LayoutComponentType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "LeadConvert");
            cachedSerQNames.add(qName);
            cls = LeadConvert.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "LeadConvertResult");
            cachedSerQNames.add(qName);
            cls = LeadConvertResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "LoginResult");
            cachedSerQNames.add(qName);
            cls = LoginResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "MassEmailMessage");
            cachedSerQNames.add(qName);
            cls = MassEmailMessage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "MergeRequest");
            cachedSerQNames.add(qName);
            cls = MergeRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "MergeResult");
            cachedSerQNames.add(qName);
            cls = MergeResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "PackageVersion");
            cachedSerQNames.add(qName);
            cls = PackageVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "PicklistEntry");
            cachedSerQNames.add(qName);
            cls = PicklistEntry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "PicklistForRecordType");
            cachedSerQNames.add(qName);
            cls = PicklistForRecordType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ProcessRequest");
            cachedSerQNames.add(qName);
            cls = ProcessRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ProcessResult");
            cachedSerQNames.add(qName);
            cls = ProcessResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ProcessSubmitRequest");
            cachedSerQNames.add(qName);
            cls = ProcessSubmitRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ProcessWorkitemRequest");
            cachedSerQNames.add(qName);
            cls = ProcessWorkitemRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "QueryLocator");
            cachedSerQNames.add(qName);
            cls = String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "QueryResult");
            cachedSerQNames.add(qName);
            cls = QueryResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "RecordTypeInfo");
            cachedSerQNames.add(qName);
            cls = RecordTypeInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "RecordTypeMapping");
            cachedSerQNames.add(qName);
            cls = RecordTypeMapping.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "RelatedList");
            cachedSerQNames.add(qName);
            cls = RelatedList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "RelatedListColumn");
            cachedSerQNames.add(qName);
            cls = RelatedListColumn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "RelatedListSort");
            cachedSerQNames.add(qName);
            cls = RelatedListSort.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "ResetPasswordResult");
            cachedSerQNames.add(qName);
            cls = ResetPasswordResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SaveResult");
            cachedSerQNames.add(qName);
            cls = SaveResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SearchRecord");
            cachedSerQNames.add(qName);
            cls = SearchRecord.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SearchResult");
            cachedSerQNames.add(qName);
            cls = SearchResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SendEmailError");
            cachedSerQNames.add(qName);
            cls = SendEmailError.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SendEmailResult");
            cachedSerQNames.add(qName);
            cls = SendEmailResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SetPasswordResult");
            cachedSerQNames.add(qName);
            cls = SetPasswordResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SingleEmailMessage");
            cachedSerQNames.add(qName);
            cls = SingleEmailMessage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "soapType");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.SoapType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "StatusCode");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.StatusCode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "UndeleteResult");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.UndeleteResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "UpsertResult");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.UpsertResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:sobject.partner.soap.sforce.com", "sObject");
            cachedSerQNames.add(qName);
            cls = com.sforce.soap.partner.sobject.SObject.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        Class cls = (Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            Class sf = (Class)
                                 cachedSerFactories.get(i);
                            Class df = (Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public LoginResult login(String username, String password) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.LoginFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "login"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {username, password});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (LoginResult) _resp;
            } catch (Exception _exception) {
                return (LoginResult) org.apache.axis.utils.JavaUtils.convert(_resp, LoginResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.LoginFault) {
              throw (com.sforce.soap.partner.fault.LoginFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public DescribeSObjectResult describeSObject(String sObjectType) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObject"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {sObjectType});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (DescribeSObjectResult) _resp;
            } catch (Exception _exception) {
                return (DescribeSObjectResult) org.apache.axis.utils.JavaUtils.convert(_resp, DescribeSObjectResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public DescribeSObjectResult[] describeSObjects(String[] sObjectType) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSObjects"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {sObjectType});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (DescribeSObjectResult[]) _resp;
            } catch (Exception _exception) {
                return (DescribeSObjectResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, DescribeSObjectResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public DescribeGlobalResult describeGlobal() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeGlobal"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (DescribeGlobalResult) _resp;
            } catch (Exception _exception) {
                return (DescribeGlobalResult) org.apache.axis.utils.JavaUtils.convert(_resp, DescribeGlobalResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public DescribeLayoutResult describeLayout(String sObjectType, String[] recordTypeIds) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeLayout"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {sObjectType, recordTypeIds});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (DescribeLayoutResult) _resp;
            } catch (Exception _exception) {
                return (DescribeLayoutResult) org.apache.axis.utils.JavaUtils.convert(_resp, DescribeLayoutResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public DescribeSoftphoneLayoutResult describeSoftphoneLayout() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeSoftphoneLayout"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (DescribeSoftphoneLayoutResult) _resp;
            } catch (Exception _exception) {
                return (DescribeSoftphoneLayoutResult) org.apache.axis.utils.JavaUtils.convert(_resp, DescribeSoftphoneLayoutResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public DescribeTabSetResult[] describeTabs() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "describeTabs"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (DescribeTabSetResult[]) _resp;
            } catch (Exception _exception) {
                return (DescribeTabSetResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, DescribeTabSetResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public SaveResult[] create(com.sforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidFieldFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "create"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {sObjects});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (SaveResult[]) _resp;
            } catch (Exception _exception) {
                return (SaveResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, SaveResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidFieldFault) {
              throw (com.sforce.soap.partner.fault.InvalidFieldFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public SaveResult[] update(com.sforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidFieldFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "update"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {sObjects});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (SaveResult[]) _resp;
            } catch (Exception _exception) {
                return (SaveResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, SaveResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidFieldFault) {
              throw (com.sforce.soap.partner.fault.InvalidFieldFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.sforce.soap.partner.UpsertResult[] upsert(String externalIDFieldName, com.sforce.soap.partner.sobject.SObject[] sObjects) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidFieldFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "upsert"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {externalIDFieldName, sObjects});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.sforce.soap.partner.UpsertResult[]) _resp;
            } catch (Exception _exception) {
                return (com.sforce.soap.partner.UpsertResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap.partner.UpsertResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidFieldFault) {
              throw (com.sforce.soap.partner.fault.InvalidFieldFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public MergeResult[] merge(MergeRequest[] request) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidFieldFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "merge"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (MergeResult[]) _resp;
            } catch (Exception _exception) {
                return (MergeResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, MergeResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidFieldFault) {
              throw (com.sforce.soap.partner.fault.InvalidFieldFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public DeleteResult[] delete(String[] ids) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "delete"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {ids});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (DeleteResult[]) _resp;
            } catch (Exception _exception) {
                return (DeleteResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, DeleteResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.sforce.soap.partner.UndeleteResult[] undelete(String[] ids) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "undelete"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {ids});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.sforce.soap.partner.UndeleteResult[]) _resp;
            } catch (Exception _exception) {
                return (com.sforce.soap.partner.UndeleteResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap.partner.UndeleteResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public EmptyRecycleBinResult[] emptyRecycleBin(String[] ids) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "emptyRecycleBin"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {ids});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (EmptyRecycleBinResult[]) _resp;
            } catch (Exception _exception) {
                return (EmptyRecycleBinResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, EmptyRecycleBinResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.sforce.soap.partner.sobject.SObject[] retrieve(String fieldList, String sObjectType, String[] ids) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.MalformedQueryFault, com.sforce.soap.partner.fault.InvalidFieldFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "retrieve"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {fieldList, sObjectType, ids});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.sforce.soap.partner.sobject.SObject[]) _resp;
            } catch (Exception _exception) {
                return (com.sforce.soap.partner.sobject.SObject[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.sforce.soap.partner.sobject.SObject[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.MalformedQueryFault) {
              throw (com.sforce.soap.partner.fault.MalformedQueryFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidFieldFault) {
              throw (com.sforce.soap.partner.fault.InvalidFieldFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public ProcessResult[] process(ProcessRequest[] actions) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidIdFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "process"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {actions});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ProcessResult[]) _resp;
            } catch (Exception _exception) {
                return (ProcessResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, ProcessResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public LeadConvertResult[] convertLead(LeadConvert[] leadConverts) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "convertLead"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {leadConverts});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (LeadConvertResult[]) _resp;
            } catch (Exception _exception) {
                return (LeadConvertResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, LeadConvertResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void logout() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "logout"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public InvalidateSessionsResult[] invalidateSessions(String[] sessionIds) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "invalidateSessions"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {sessionIds});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (InvalidateSessionsResult[]) _resp;
            } catch (Exception _exception) {
                return (InvalidateSessionsResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, InvalidateSessionsResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public GetDeletedResult getDeleted(String sObjectType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[19]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getDeleted"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {sObjectType, startDate, endDate});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (GetDeletedResult) _resp;
            } catch (Exception _exception) {
                return (GetDeletedResult) org.apache.axis.utils.JavaUtils.convert(_resp, GetDeletedResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public GetUpdatedResult getUpdated(String sObjectType, java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[20]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUpdated"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {sObjectType, startDate, endDate});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (GetUpdatedResult) _resp;
            } catch (Exception _exception) {
                return (GetUpdatedResult) org.apache.axis.utils.JavaUtils.convert(_resp, GetUpdatedResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public QueryResult query(String queryString) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidQueryLocatorFault, com.sforce.soap.partner.fault.MalformedQueryFault, com.sforce.soap.partner.fault.InvalidFieldFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[21]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "query"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {queryString});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (QueryResult) _resp;
            } catch (Exception _exception) {
                return (QueryResult) org.apache.axis.utils.JavaUtils.convert(_resp, QueryResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidQueryLocatorFault) {
              throw (com.sforce.soap.partner.fault.InvalidQueryLocatorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.MalformedQueryFault) {
              throw (com.sforce.soap.partner.fault.MalformedQueryFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidFieldFault) {
              throw (com.sforce.soap.partner.fault.InvalidFieldFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public QueryResult queryAll(String queryString) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidQueryLocatorFault, com.sforce.soap.partner.fault.MalformedQueryFault, com.sforce.soap.partner.fault.InvalidFieldFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[22]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryAll"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {queryString});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (QueryResult) _resp;
            } catch (Exception _exception) {
                return (QueryResult) org.apache.axis.utils.JavaUtils.convert(_resp, QueryResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidQueryLocatorFault) {
              throw (com.sforce.soap.partner.fault.InvalidQueryLocatorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.MalformedQueryFault) {
              throw (com.sforce.soap.partner.fault.MalformedQueryFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidFieldFault) {
              throw (com.sforce.soap.partner.fault.InvalidFieldFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public QueryResult queryMore(String queryLocator) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidQueryLocatorFault, com.sforce.soap.partner.fault.InvalidFieldFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[23]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "queryMore"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {queryLocator});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (QueryResult) _resp;
            } catch (Exception _exception) {
                return (QueryResult) org.apache.axis.utils.JavaUtils.convert(_resp, QueryResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidQueryLocatorFault) {
              throw (com.sforce.soap.partner.fault.InvalidQueryLocatorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidFieldFault) {
              throw (com.sforce.soap.partner.fault.InvalidFieldFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public SearchResult search(String searchString) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.MalformedSearchFault, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidSObjectFault, com.sforce.soap.partner.fault.InvalidFieldFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[24]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "search"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {searchString});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (SearchResult) _resp;
            } catch (Exception _exception) {
                return (SearchResult) org.apache.axis.utils.JavaUtils.convert(_resp, SearchResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.MalformedSearchFault) {
              throw (com.sforce.soap.partner.fault.MalformedSearchFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidSObjectFault) {
              throw (com.sforce.soap.partner.fault.InvalidSObjectFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidFieldFault) {
              throw (com.sforce.soap.partner.fault.InvalidFieldFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public GetServerTimestampResult getServerTimestamp() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[25]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getServerTimestamp"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (GetServerTimestampResult) _resp;
            } catch (Exception _exception) {
                return (GetServerTimestampResult) org.apache.axis.utils.JavaUtils.convert(_resp, GetServerTimestampResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public SetPasswordResult setPassword(String userId, String password) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidIdFault, com.sforce.soap.partner.fault.InvalidNewPasswordFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[26]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "setPassword"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {userId, password});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (SetPasswordResult) _resp;
            } catch (Exception _exception) {
                return (SetPasswordResult) org.apache.axis.utils.JavaUtils.convert(_resp, SetPasswordResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidNewPasswordFault) {
              throw (com.sforce.soap.partner.fault.InvalidNewPasswordFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public ResetPasswordResult resetPassword(String userId) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault, com.sforce.soap.partner.fault.InvalidIdFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[27]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "resetPassword"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {userId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (ResetPasswordResult) _resp;
            } catch (Exception _exception) {
                return (ResetPasswordResult) org.apache.axis.utils.JavaUtils.convert(_resp, ResetPasswordResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.InvalidIdFault) {
              throw (com.sforce.soap.partner.fault.InvalidIdFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public GetUserInfoResult getUserInfo() throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[28]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "getUserInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (GetUserInfoResult) _resp;
            } catch (Exception _exception) {
                return (GetUserInfoResult) org.apache.axis.utils.JavaUtils.convert(_resp, GetUserInfoResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public SendEmailResult[] sendEmail(Email[] messages) throws java.rmi.RemoteException, com.sforce.soap.partner.fault.UnexpectedErrorFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[29]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sendEmail"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        Object _resp = _call.invoke(new Object[] {messages});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (SendEmailResult[]) _resp;
            } catch (Exception _exception) {
                return (SendEmailResult[]) org.apache.axis.utils.JavaUtils.convert(_resp, SendEmailResult[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.sforce.soap.partner.fault.UnexpectedErrorFault) {
              throw (com.sforce.soap.partner.fault.UnexpectedErrorFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
