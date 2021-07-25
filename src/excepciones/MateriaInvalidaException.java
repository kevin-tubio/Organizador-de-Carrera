package excepciones;

public class MateriaInvalidaException extends Exception {

	private static final long serialVersionUID = -8910931313520194514L;

	public MateriaInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public MateriaInvalidaException(String message) {
		super(message);
	}

}
