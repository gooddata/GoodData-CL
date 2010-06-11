package com.gooddata.exception;

/**
 * Invalid argument exception
 *
 * @author Zdenek Svoboda <zd@gooddata.org>
 * @version 1.0
 */
public class InvalidArgumentException extends Exception {
    /**
     * Constructor
     * @param s exception message
     */
    public InvalidArgumentException(String s) {
    	super(s);
    }
}
