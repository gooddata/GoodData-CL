package com.gooddata.exception;

/**
 * @author jiri.zaloudek
 */
public class GdcSSTokenException extends GdcProjectAccessException {


    /**
     * Creates a new instance of <code>GdcProjectAccessException</code> without detail message.
     */
    public GdcSSTokenException() {
    }


    /**
     * Constructs an instance of <code>GdcProjectAccessException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public GdcSSTokenException(String msg) {
        super(msg);
    }
}
