/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gooddata.exception;

/**
 * @author jiri.zaloudek
 */
public class HttpMethodException extends GdcRestApiException {


    /**
     * Constructs an instance of <code>HttpMethodException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public HttpMethodException(String msg) {
        super(msg);
    }
}
