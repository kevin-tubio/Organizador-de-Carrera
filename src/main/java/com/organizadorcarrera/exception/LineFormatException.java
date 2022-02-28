package com.organizadorcarrera.exception;

public class LineFormatException extends FileException {

	private static final long serialVersionUID = -52956953333060245L;

	public LineFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public LineFormatException(String message) {
		super(message);
	}

}
