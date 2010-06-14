/**
 * LayoutComponentType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class LayoutComponentType implements java.io.Serializable {
    private String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected LayoutComponentType(String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final String _Field = "Field";
    public static final String _Separator = "Separator";
    public static final String _SControl = "SControl";
    public static final String _EmptySpace = "EmptySpace";
    public static final LayoutComponentType Field = new LayoutComponentType(_Field);
    public static final LayoutComponentType Separator = new LayoutComponentType(_Separator);
    public static final LayoutComponentType SControl = new LayoutComponentType(_SControl);
    public static final LayoutComponentType EmptySpace = new LayoutComponentType(_EmptySpace);
    public String getValue() { return _value_;}
    public static LayoutComponentType fromValue(String value)
          throws IllegalArgumentException {
        LayoutComponentType enumeration = (LayoutComponentType)
            _table_.get(value);
        if (enumeration==null) throw new IllegalArgumentException();
        return enumeration;
    }
    public static LayoutComponentType fromString(String value)
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
        new org.apache.axis.description.TypeDesc(LayoutComponentType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "layoutComponentType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
