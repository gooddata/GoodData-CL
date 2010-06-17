package com.gooddata.exception;

/**
 * GoodData internal error
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class InternalErrorException extends GdcException {

    /**
     * Exception constructor
     * @param msg exception message
     */
    public InternalErrorException(String msg) {
        super(msg);
    }
    
    /**
     * Exception constructor
     * @param msg exception message
     * @param cause root cause
     */
    public InternalErrorException(String msg, Throwable cause) {
    	super(msg, cause);
    }

}