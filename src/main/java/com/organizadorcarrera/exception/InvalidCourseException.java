package com.organizadorcarrera.exception;

public class InvalidCourseException extends Exception {

	private static final long serialVersionUID = -8910931313520194514L;

	public InvalidCourseException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCourseException(String message) {
		super(message);
	}

}
