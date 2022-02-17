package com.organizadorcarrera.enumerados;

import com.organizadorcarrera.util.LangResource;

public enum Tipo {
	MATERIA("TipoMateria"), TESIS("TipoTesis"), SEMINARIO_ELECTIVO("TipoSeminarioElectivo"),
	SEMINARIO_OPTATIVO("TipoSeminarioOptativo"), ASIGNATURA_ELECTIVA("TipoAsignaturaElectiva"),
	IDIOMA_EXTRANJERO("TipoIdiomaExtranjero");

	private String mensaje;

	private Tipo(String string) {
		this.mensaje = LangResource.getString(string);
	}

	@Override
	public String toString() {
		return this.mensaje;
	}
}
