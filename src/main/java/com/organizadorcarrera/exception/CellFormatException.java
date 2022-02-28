package com.organizadorcarrera.exception;

public class CellFormatException extends PlanillaInvalidaException {

	private static final long serialVersionUID = -3370512511107656679L;

	public CellFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public CellFormatException(String message) {
		super(message);
	}

}
