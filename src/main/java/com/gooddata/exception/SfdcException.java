package com.gooddata.exception;

public class SfdcException extends Exception {
	
	String message;
	
	public SfdcException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
