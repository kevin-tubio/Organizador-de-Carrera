package com.organizadorcarrera.exception;

public class EmptyFileException extends FileException {

	private static final long serialVersionUID = 3505800020437969665L;

	public EmptyFileException(String message) {
		super(message);
	}

	public EmptyFileException(String message, Throwable cause) {
		super(message, cause);
	}

}
