/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gooddata.exception;

/**
 * @author jiri.zaloudek
 */
public class GdcIntegrationErrorException extends GdcRestApiException {


    /**
     * Constructs an instance of <code>GdcIntegrationErrorException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GdcIntegrationErrorException(String msg) {
        super(msg);
    }

    public GdcIntegrationErrorException(Throwable cause) {
        super(cause);
    }

    public GdcIntegrationErrorException(String msg, Throwable cause) {
    	super(msg, cause);
    }
}
