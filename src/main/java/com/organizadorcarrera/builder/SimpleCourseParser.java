package com.organizadorcarrera.builder;

import com.organizadorcarrera.enums.CourseType;
import com.organizadorcarrera.exception.CellFormatException;
import com.organizadorcarrera.util.LangResource;
import org.springframework.stereotype.Component;

@Component
public class SimpleCourseParser extends CourseParser {

	@Override
	protected String obtenerNombreMateria(String contenido) {
		return contenido.split(" ?\\([\\d]+\\)")[0];
	}

	@Override
	protected CourseType obtenerTipoDeMateria(String contenido) throws CellFormatException {
		contenido = contenido.strip();
		if (contenido.equals(LangResource.getString("TipoMateria")))
			return CourseType.MATERIA;

		if (contenido.equals(LangResource.getString("TipoTesis")))
			return CourseType.TESIS;

		throw new CellFormatException(LangResource.getString("TipoMateriaInvalida"));
	}

}
