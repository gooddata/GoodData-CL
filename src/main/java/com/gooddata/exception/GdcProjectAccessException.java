/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gooddata.exception;


/**
 * @author jiri.zaloudek
 */
public class GdcProjectAccessException extends GdcRestApiException {

    /**
     * Constructs an instance of <code>GdcProjectAccessException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GdcProjectAccessException(String msg) {
        super(msg);
    }
}
