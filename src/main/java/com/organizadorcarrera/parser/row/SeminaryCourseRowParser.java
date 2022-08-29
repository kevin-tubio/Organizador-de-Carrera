package com.organizadorcarrera.parser.row;

import org.apache.poi.ss.usermodel.Row;

import com.organizadorcarrera.enums.CourseType;
import com.organizadorcarrera.exception.CellFormatException;
import com.organizadorcarrera.util.LangResource;

import org.springframework.stereotype.Component;

@Component
public class SeminaryCourseRowParser extends RowParser {

	@Override
	protected String parseCourseName(Row row) {
		var rowContent = super.parseCourseName(row);
		rowContent = rowContent.split(":")[1].strip();
		return rowContent.replace("\"", "");
	}

	@Override
	protected CourseType parseCourseType(Row row) throws CellFormatException {
		var rowContent = row.getCell(0).getStringCellValue().split(":")[0];
		rowContent = rowContent.strip();
		if (rowContent.equals(LangResource.getString("TipoSeminarioOptativo")))
			return CourseType.SEMINARIO_OPTATIVO;

		if (rowContent.equals(LangResource.getString("TipoSeminarioElectivo")))
			return CourseType.SEMINARIO_ELECTIVO;

		if (rowContent.equals(LangResource.getString("TipoAsignaturaElectiva")))
			return CourseType.ASIGNATURA_ELECTIVA;

		throw new CellFormatException(LangResource.getString("TipoMateriaInvalida"));
	}

}
