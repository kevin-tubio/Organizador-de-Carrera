package com.organizadorcarrera.parser.row;

import com.organizadorcarrera.enums.CourseType;
import com.organizadorcarrera.exception.CellFormatException;

import org.apache.poi.ss.usermodel.Row;

import org.springframework.stereotype.Component;

@Component
public class ForeignLanguageCourseRowParser extends RowParser {

	@Override
	protected String parseCourseName(Row row) {
		var rowContent = super.parseCourseName(row);
		rowContent = rowContent.split("\\(")[1];
		return rowContent.replace(")", "");
	}

	@Override
	protected CourseType parseCourseType(Row row) throws CellFormatException {
		return CourseType.IDIOMA_EXTRANJERO;
	}

}
