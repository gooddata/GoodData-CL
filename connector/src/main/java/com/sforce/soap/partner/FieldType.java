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
 * FieldType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class FieldType implements java.io.Serializable {
    private String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected FieldType(String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final String _value1 = "string";
    public static final String _value2 = "picklist";
    public static final String _value3 = "multipicklist";
    public static final String _value4 = "combobox";
    public static final String _value5 = "reference";
    public static final String _value6 = "base64";
    public static final String _value7 = "boolean";
    public static final String _value8 = "currency";
    public static final String _value9 = "textarea";
    public static final String _value10 = "int";
    public static final String _value11 = "double";
    public static final String _value12 = "percent";
    public static final String _value13 = "phone";
    public static final String _value14 = "id";
    public static final String _value15 = "date";
    public static final String _value16 = "datetime";
    public static final String _value17 = "time";
    public static final String _value18 = "url";
    public static final String _value19 = "email";
    public static final String _value20 = "encryptedstring";
    public static final String _value21 = "anyType";
    public static final FieldType value1 = new FieldType(_value1);
    public static final FieldType value2 = new FieldType(_value2);
    public static final FieldType value3 = new FieldType(_value3);
    public static final FieldType value4 = new FieldType(_value4);
    public static final FieldType value5 = new FieldType(_value5);
    public static final FieldType value6 = new FieldType(_value6);
    public static final FieldType value7 = new FieldType(_value7);
    public static final FieldType value8 = new FieldType(_value8);
    public static final FieldType value9 = new FieldType(_value9);
    public static final FieldType value10 = new FieldType(_value10);
    public static final FieldType value11 = new FieldType(_value11);
    public static final FieldType value12 = new FieldType(_value12);
    public static final FieldType value13 = new FieldType(_value13);
    public static final FieldType value14 = new FieldType(_value14);
    public static final FieldType value15 = new FieldType(_value15);
    public static final FieldType value16 = new FieldType(_value16);
    public static final FieldType value17 = new FieldType(_value17);
    public static final FieldType value18 = new FieldType(_value18);
    public static final FieldType value19 = new FieldType(_value19);
    public static final FieldType value20 = new FieldType(_value20);
    public static final FieldType value21 = new FieldType(_value21);
    public String getValue() { return _value_;}
    public static FieldType fromValue(String value)
          throws IllegalArgumentException {
        FieldType enumeration = (FieldType)
            _table_.get(value);
        if (enumeration==null) throw new IllegalArgumentException();
        return enumeration;
    }
    public static FieldType fromString(String value)
          throws IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public String toString() { return _value_;}
    public Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FieldType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "fieldType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
