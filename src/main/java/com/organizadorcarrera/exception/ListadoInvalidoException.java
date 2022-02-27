package com.organizadorcarrera.exception;

public class ListadoInvalidoException extends Exception {

	private static final long serialVersionUID = 7568282147724212611L;

	public ListadoInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ListadoInvalidoException(String message) {
		super(message);
	}

}
