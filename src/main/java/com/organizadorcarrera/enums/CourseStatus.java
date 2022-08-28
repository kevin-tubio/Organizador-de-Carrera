package com.organizadorcarrera.enums;

import com.organizadorcarrera.util.LangResource;

public enum CourseStatus {

	NO_CURSADA("EstadoNoCursado"),
	APROBADA("EstadoAprobado"),
	REGULARIZADA("EstadoRegularizado"),
	ENROLLED("EstadoEnCurso"),
	EQUIVALENCIA("EstadoEquivalencia");

	private final String string;

	CourseStatus(String string) {
		this.string = LangResource.getString(string);
	}

	@Override
	public String toString() {
		return string;
	}

}
