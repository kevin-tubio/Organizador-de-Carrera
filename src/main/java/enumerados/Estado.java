package enumerados;

import util.LangResource;

public enum Estado {
	NO_CURSADA("EstadoNoCursado"), APROBADA("EstadoAprobado"), REGULARIZADA("EstadoRegularizado"),
	EN_CURSO("EstadoEnCurso"), EQUIVALENCIA("EstadoEquivalencia");


	private String cadena;

	private Estado(String string) {
		this.cadena = LangResource.getString(string);
	}

	@Override
	public String toString() {
		return cadena;
	}

}
