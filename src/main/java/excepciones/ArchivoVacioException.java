package excepciones;

public class ArchivoVacioException extends ArchivoException {

	private static final long serialVersionUID = 3505800020437969665L;

	public ArchivoVacioException(String message) {
		super(message);
	}

	public ArchivoVacioException(String message, Throwable cause) {
		super(message, cause);
	}

}
