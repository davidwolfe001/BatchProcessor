package com.os.project1.Exceptions;

@SuppressWarnings("serial")
public class ProcessException extends RuntimeException{

	public ProcessException(String message) {
		super(message);
	}
	
	public ProcessException(String message, Throwable cause) {
		super(message,cause);
	}

}
