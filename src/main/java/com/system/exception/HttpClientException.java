package com.system.exception;

public class HttpClientException extends RuntimeException{

	private static final long serialVersionUID = -2503345001841814995L;

	
	public HttpClientException(String message, Throwable t) {
        super(message, t);
    }
    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(Throwable t) {
        super(t);
    }
}