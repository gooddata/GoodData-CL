/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gooddata.exceptions;

/**
 * @author jiri.zaloudek
 */
public class HttpMethodException extends GdcRestApiException {

    /**
     * Creates a new instance of <code>HttpMethodException</code> without detail message.
     */
    public HttpMethodException() {
    }


    /**
     * Constructs an instance of <code>HttpMethodException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public HttpMethodException(String msg) {
        super(msg);
    }
}
