package com.gooddata.exception;

/**
 * GoodData initialization exception
 *
 * @author zd <zd@gooddata.com>
 * @version 1.0
 */
public class InitializationException extends GdcException {

    /**
     * Exception constructor
     * @param msg exception message
     */
    public InitializationException(String msg) {
        super(msg);
    }

}