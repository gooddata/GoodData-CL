package com.gooddata.exception;

public class SfdcException extends GdcException {
	

	public SfdcException(String message) {
		super(message);
	}

    public SfdcException(String message, Throwable e) {
		super(message,e);
	}

    public SfdcException(Throwable e) {
		super(e);
	}

}
