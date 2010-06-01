/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gooddata.integration.rest.exceptions;

/**
 * @author jiri.zaloudek
 */
public class GdcLoginException extends GdcProjectAccessException {


    /**
     * Creates a new instance of <code>GdcProjectAccessException</code> without detail message.
     */
    public GdcLoginException() {
    }


    /**
     * Constructs an instance of <code>GdcProjectAccessException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GdcLoginException(String msg) {
        super(msg);
    }
}
