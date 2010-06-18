/**
 * SearchResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sforce.soap.partner;

public class SearchResult  implements java.io.Serializable {
    private SearchRecord[] searchRecords;

    private String sforceReserved;

    public SearchResult() {
    }

    public SearchResult(
           SearchRecord[] searchRecords,
           String sforceReserved) {
           this.searchRecords = searchRecords;
           this.sforceReserved = sforceReserved;
    }


    /**
     * Gets the searchRecords value for this SearchResult.
     * 
     * @return searchRecords
     */
    public SearchRecord[] getSearchRecords() {
        return searchRecords;
    }


    /**
     * Sets the searchRecords value for this SearchResult.
     * 
     * @param searchRecords
     */
    public void setSearchRecords(SearchRecord[] searchRecords) {
        this.searchRecords = searchRecords;
    }

    public SearchRecord getSearchRecords(int i) {
        return this.searchRecords[i];
    }

    public void setSearchRecords(int i, SearchRecord _value) {
        this.searchRecords[i] = _value;
    }


    /**
     * Gets the sforceReserved value for this SearchResult.
     * 
     * @return sforceReserved
     */
    public String getSforceReserved() {
        return sforceReserved;
    }


    /**
     * Sets the sforceReserved value for this SearchResult.
     * 
     * @param sforceReserved
     */
    public void setSforceReserved(String sforceReserved) {
        this.sforceReserved = sforceReserved;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof SearchResult)) return false;
        SearchResult other = (SearchResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.searchRecords==null && other.getSearchRecords()==null) || 
             (this.searchRecords!=null &&
              java.util.Arrays.equals(this.searchRecords, other.getSearchRecords()))) &&
            ((this.sforceReserved==null && other.getSforceReserved()==null) || 
             (this.sforceReserved!=null &&
              this.sforceReserved.equals(other.getSforceReserved())));
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
        if (getSearchRecords() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSearchRecords());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getSearchRecords(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSforceReserved() != null) {
            _hashCode += getSforceReserved().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SearchResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchRecords");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "searchRecords"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "SearchRecord"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sforceReserved");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:partner.soap.sforce.com", "sforceReserved"));
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
