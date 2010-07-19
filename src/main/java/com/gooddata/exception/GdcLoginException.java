/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gooddata.exception;

/**
 * @author jiri.zaloudek
 */
public class GdcLoginException extends GdcProjectAccessException {


    /**
     * Constructs an instance of <code>GdcProjectAccessException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GdcLoginException(String msg) {
        super(msg);
    }
    
    public GdcLoginException(String msg, Throwable cause) {
    	super(msg, cause);
    }
}
