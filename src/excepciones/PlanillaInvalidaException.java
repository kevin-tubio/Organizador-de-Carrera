package excepciones;

public class PlanillaInvalidaException extends Exception {

	private static final long serialVersionUID = 7436271155743279782L;

	public PlanillaInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlanillaInvalidaException(String message) {
		super(message);
	}

}
