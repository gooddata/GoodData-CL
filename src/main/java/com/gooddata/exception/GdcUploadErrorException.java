/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gooddata.exception;

/**
 * @author jiri.zaloudek
 */
public class GdcUploadErrorException extends GdcRestApiException {


    /**
     * Constructs an instance of <code>GdcIntegrationErrorException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GdcUploadErrorException(String msg) {
        super(msg);
    }
}
