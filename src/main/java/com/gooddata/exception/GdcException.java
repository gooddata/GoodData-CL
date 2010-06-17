package com.gooddata.exception;

/**
 * GoodData root exception
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class GdcException extends RuntimeException {

    public GdcException(String msg) {
        super(msg);
    }

    public GdcException(Throwable cause) {
        super(cause);
    }
    
    public GdcException(String msg, Throwable cause) {
    	super(msg, cause);
    }

}
