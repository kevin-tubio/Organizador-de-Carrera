package enumerados;

public enum Estado {
	NO_CURSADA("No cursada"), APROBADA("Aprobada"), REGULARIZADA("Regularizada"), EN_CURSO("En curso");

	private String cadena;

	private Estado(String string) {
		this.cadena = string;
	}

	@Override
	public String toString() {
		return cadena;
	}
}
