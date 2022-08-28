package com.organizadorcarrera.builder;

import org.apache.poi.ss.usermodel.Row;

import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.enums.CourseType;
import com.organizadorcarrera.exception.CellFormatException;
import com.organizadorcarrera.util.LangResource;
import org.springframework.stereotype.Component;

@Component
public class SeminaryCourseParser extends CourseParser {

	@Override
	public Course crearMateria(Row filaActual) throws CellFormatException {
		var nombre = obtenerNombreMateria(filaActual.getCell(0).getStringCellValue());
		var materia = super.generarMateria(nombre, filaActual);
		materia.setCourseType(obtenerTipoDeMateria(filaActual.getCell(0).getStringCellValue().split(":")[0]));
		return materia;
	}

	@Override
	protected String obtenerNombreMateria(String contenido) {
		contenido = super.obtenerNombreMateria(contenido);
		contenido = contenido.split(":")[1].strip();
		return contenido.replace("\"", "");
	}

	@Override
	protected CourseType obtenerTipoDeMateria(String contenido) throws CellFormatException {
		contenido = contenido.strip();
		if (contenido.equals(LangResource.getString("TipoSeminarioOptativo")))
			return CourseType.SEMINARIO_OPTATIVO;

		if (contenido.equals(LangResource.getString("TipoSeminarioElectivo")))
			return CourseType.SEMINARIO_ELECTIVO;

		if (contenido.equals(LangResource.getString("TipoAsignaturaElectiva")))
			return CourseType.ASIGNATURA_ELECTIVA;

		throw new CellFormatException(LangResource.getString("TipoMateriaInvalida"));
	}

}
