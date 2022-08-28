package com.organizadorcarrera.parser.row;

import com.organizadorcarrera.enums.CourseType;
import com.organizadorcarrera.exception.CellFormatException;
import org.springframework.stereotype.Component;

@Component
public class ForeignLanguageCourseRowParser extends RowParser {

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
