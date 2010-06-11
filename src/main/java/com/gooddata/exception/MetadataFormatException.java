package com.gooddata.exception;

/**
 * GoodData metadata format exception
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class MetadataFormatException extends Exception {

    /**
     * Exception constructor
     * @param msg exception message
     */
    public MetadataFormatException(String msg) {
        super(msg);
    }

}
