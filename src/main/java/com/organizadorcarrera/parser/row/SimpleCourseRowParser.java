package com.organizadorcarrera.parser.row;

import com.organizadorcarrera.enums.CourseType;
import com.organizadorcarrera.exception.CellFormatException;
import com.organizadorcarrera.util.LangResource;

import org.apache.poi.ss.usermodel.Row;

import org.springframework.stereotype.Component;

@Component
public class SimpleCourseRowParser extends RowParser {

	@Override
	protected String parseCourseName(Row row) {
		var rowContent = super.parseCourseName(row);
		return rowContent.split(" ?\\(\\d+\\)")[0];
	}

	@Override
	protected CourseType parseCourseType(Row row) throws CellFormatException {
		var rowContent = row.getCell(1).getStringCellValue().strip();
		if (rowContent.equals(LangResource.getString("TipoMateria")))
			return CourseType.MATERIA;

		if (rowContent.equals(LangResource.getString("TipoTesis")))
			return CourseType.TESIS;

		throw new CellFormatException(LangResource.getString("TipoMateriaInvalida"));
	}

}
