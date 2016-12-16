package com.it.audit.exception;

public class NotLoginException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NotLoginException(String message) {
        super(message);
    }
}
