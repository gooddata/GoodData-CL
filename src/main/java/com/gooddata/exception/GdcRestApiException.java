/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gooddata.exception;

/**
 * @author jiri.zaloudek
 */
public class GdcRestApiException extends GdcException {


    /**
     * Constructs an instance of <code>GdcRestApiExecption</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GdcRestApiException(String msg) {
        super(msg);
    }

    public GdcRestApiException(Throwable cause) {
        super(cause);
    }

    public GdcRestApiException(String msg, Throwable cause) {
    	super(msg, cause);
    }

    @Override
    public String getLocalizedMessage() {
        // TODO Auto-generated method stub
        return super.getLocalizedMessage();
    }


    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return super.getMessage();
    }


    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }


}
