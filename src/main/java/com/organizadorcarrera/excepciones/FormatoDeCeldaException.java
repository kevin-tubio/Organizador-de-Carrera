package com.organizadorcarrera.excepciones;

public class FormatoDeCeldaException extends PlanillaInvalidaException {

	private static final long serialVersionUID = -3370512511107656679L;

	public FormatoDeCeldaException(String message, Throwable cause) {
		super(message, cause);
	}

	public FormatoDeCeldaException(String message) {
		super(message);
	}

}
