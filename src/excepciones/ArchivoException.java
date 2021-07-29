package excepciones;

public class ArchivoException extends Exception {

	private static final long serialVersionUID = 658993628374629329L;

	public ArchivoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArchivoException(String message) {
		super(message);
	}

}
