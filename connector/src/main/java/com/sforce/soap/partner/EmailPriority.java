/**
 * EmailPriority.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class EmailPriority implements java.io.Serializable {
    private String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected EmailPriority(String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final String _Highest = "Highest";
    public static final String _High = "High";
    public static final String _Normal = "Normal";
    public static final String _Low = "Low";
    public static final String _Lowest = "Lowest";
    public static final EmailPriority Highest = new EmailPriority(_Highest);
    public static final EmailPriority High = new EmailPriority(_High);
    public static final EmailPriority Normal = new EmailPriority(_Normal);
    public static final EmailPriority Low = new EmailPriority(_Low);
    public static final EmailPriority Lowest = new EmailPriority(_Lowest);
    public String getValue() { return _value_;}
    public static EmailPriority fromValue(String value)
          throws IllegalArgumentException {
        EmailPriority enumeration = (EmailPriority)
            _table_.get(value);
        if (enumeration==null) throw new IllegalArgumentException();
        return enumeration;
    }
    public static EmailPriority fromString(String value)
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
        new org.apache.axis.description.TypeDesc(EmailPriority.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "EmailPriority"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
