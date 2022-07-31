package com.organizadorcarrera.builder;

import com.organizadorcarrera.enumerados.CourseType;
import com.organizadorcarrera.exception.CellFormatException;

public class ForeignLanguageCourseParser extends CourseParser {

	@Override
	protected String obtenerNombreMateria(String contenido) {
		contenido = super.obtenerNombreMateria(contenido);
		contenido = contenido.split("\\(")[1];
		return contenido.replace(")", "");
	}

	@Override
	protected CourseType obtenerTipoDeMateria(String contenido) throws CellFormatException {
		return CourseType.IDIOMA_EXTRANJERO;
	}

}
