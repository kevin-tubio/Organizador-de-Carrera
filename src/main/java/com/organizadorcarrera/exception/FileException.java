package com.organizadorcarrera.exception;

public class FileException extends Exception {

	private static final long serialVersionUID = 658993628374629329L;

	public FileException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileException(String message) {
		super(message);
	}

}
