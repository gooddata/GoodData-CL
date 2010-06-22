/*
 * .
 */

/**
 * SoapType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class SoapType implements java.io.Serializable {
    private String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected SoapType(String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final String _value1 = "tns:ID";
    public static final String _value2 = "xsd:base64Binary";
    public static final String _value3 = "xsd:boolean";
    public static final String _value4 = "xsd:double";
    public static final String _value5 = "xsd:int";
    public static final String _value6 = "xsd:string";
    public static final String _value7 = "xsd:date";
    public static final String _value8 = "xsd:dateTime";
    public static final String _value9 = "xsd:time";
    public static final String _value10 = "xsd:anyType";
    public static final SoapType value1 = new SoapType(_value1);
    public static final SoapType value2 = new SoapType(_value2);
    public static final SoapType value3 = new SoapType(_value3);
    public static final SoapType value4 = new SoapType(_value4);
    public static final SoapType value5 = new SoapType(_value5);
    public static final SoapType value6 = new SoapType(_value6);
    public static final SoapType value7 = new SoapType(_value7);
    public static final SoapType value8 = new SoapType(_value8);
    public static final SoapType value9 = new SoapType(_value9);
    public static final SoapType value10 = new SoapType(_value10);
    public String getValue() { return _value_;}
    public static SoapType fromValue(String value)
          throws IllegalArgumentException {
        SoapType enumeration = (SoapType)
            _table_.get(value);
        if (enumeration==null) throw new IllegalArgumentException();
        return enumeration;
    }
    public static SoapType fromString(String value)
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
        new org.apache.axis.description.TypeDesc(SoapType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "soapType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
