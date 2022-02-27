package com.organizadorcarrera.exception;

public class PlanillaInvalidaException extends ArchivoException {

	private static final long serialVersionUID = 7436271155743279782L;

	public PlanillaInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlanillaInvalidaException(String message) {
		super(message);
	}

}
