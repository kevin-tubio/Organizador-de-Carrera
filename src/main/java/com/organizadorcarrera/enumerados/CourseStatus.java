package com.organizadorcarrera.enumerados;

import com.organizadorcarrera.util.LangResource;

public enum CourseStatus {

	NO_CURSADA("EstadoNoCursado"),
	APROBADA("EstadoAprobado"),
	REGULARIZADA("EstadoRegularizado"),
	ENROLLED("EstadoEnCurso"),
	EQUIVALENCIA("EstadoEquivalencia");

	private String cadena;

	private CourseStatus(String string) {
		this.cadena = LangResource.getString(string);
	}

	@Override
	public String toString() {
		return cadena;
	}

}
