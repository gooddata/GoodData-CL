/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gooddata.integration.rest.exceptions;

/**
 * @author jiri.zaloudek
 */
public class GdcUploadErrorException extends GdcRestApiException {


    /**
     * Creates a new instance of <code>GdcIntegrationErrorException</code> without detail message.
     */
    public GdcUploadErrorException() {
    }


    /**
     * Constructs an instance of <code>GdcIntegrationErrorException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GdcUploadErrorException(String msg) {
        super(msg);
    }
}
