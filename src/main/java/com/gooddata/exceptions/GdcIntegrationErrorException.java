/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gooddata.exceptions;

/**
 * @author jiri.zaloudek
 */
public class GdcIntegrationErrorException extends GdcRestApiException {


    /**
     * Creates a new instance of <code>GdcIntegrationErrorException</code> without detail message.
     */
    public GdcIntegrationErrorException() {
    }


    /**
     * Constructs an instance of <code>GdcIntegrationErrorException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GdcIntegrationErrorException(String msg) {
        super(msg);
    }
}
