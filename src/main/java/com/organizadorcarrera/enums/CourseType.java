package com.organizadorcarrera.enums;

import com.organizadorcarrera.util.LangResource;

public enum CourseType {

	MATERIA("TipoMateria"),
	TESIS("TipoTesis"),
	SEMINARIO_ELECTIVO("TipoSeminarioElectivo"),
	SEMINARIO_OPTATIVO("TipoSeminarioOptativo"),
	ASIGNATURA_ELECTIVA("TipoAsignaturaElectiva"),
	IDIOMA_EXTRANJERO("TipoIdiomaExtranjero");

	private final String message;

	CourseType(String string) {
		this.message = LangResource.getString(string);
	}

	@Override
	public String toString() {
		return this.message;
	}
}
