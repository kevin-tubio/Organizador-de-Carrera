package com.organizadorcarrera.parser.row;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.enums.CourseStatus;
import com.organizadorcarrera.enums.CoursePeriod;
import com.organizadorcarrera.enums.CourseType;
import com.organizadorcarrera.exception.CellFormatException;
import com.organizadorcarrera.util.LangResource;

public abstract class RowParser {

	public Course parseCourse(Row row) throws CellFormatException {
		var name = parseCourseName(row);
		var course = createCourse(name, row);
		course.setCourseType(parseCourseType(row));
		return course;
	}

	private Course createCourse(String name, Row row) throws CellFormatException {
		var id = parseCourseId(row.getCell(0).getStringCellValue());
		var year = parseCourseYear(row.getCell(2));
		var coursePeriod = parseCoursePeriod(row.getCell(3).getStringCellValue());
		var course = new Course(id, name, year, coursePeriod);
		course.setCourseStatus(parseCourseStatus(row));
		if (course.getCourseStatus().equals(CourseStatus.APROBADA))
			course.setGrade(parseCourseGrade(row.getCell(4).getStringCellValue()));
		course.setCreditos(parseCourseCredits(row.getCell(6).getStringCellValue()));
		return course;
	}

	private int parseCourseId(String rowContent) {
		return Integer.parseInt(rowContent.split("\\D+")[1]);
	}

	protected String parseCourseName(Row row) {
		String rowContent = row.getCell(0).getStringCellValue();
		return rowContent.split(" ?\\(\\d+\\)")[0];
	}

	private int parseCourseYear(Cell cell) throws CellFormatException {
		try {
			return Integer.parseInt(cell.getStringCellValue().strip());
		} catch (NumberFormatException e) {
			throw new CellFormatException(LangResource.getString("NumeroEsperadoColumna"), e.getCause());
		} catch (IllegalStateException e) {
			return (int) cell.getNumericCellValue();
		}
	}

	private CoursePeriod parseCoursePeriod(String rowContent) throws CellFormatException {
		if (!rowContent.matches("^([1-2A-Z][a-z]+ +)?[A-Z][a-z]+ *$"))
			throw new CellFormatException(LangResource.getString("FormatoInvalido"));

		rowContent = rowContent.replaceAll(" +", " ").strip();
		if (rowContent.equals(LangResource.getString("PeriodoPrimerCuatrimestre")))
			return CoursePeriod.PRIMER_CUATRIMESTRE;

		if (rowContent.equals(LangResource.getString("PeriodoSegundoCuatrimestre")))
			return CoursePeriod.SEGUNDO_CUATRIMESTRE;

		return CoursePeriod.ANUAL;
	}

	private CourseStatus parseCourseStatus(Row row) throws CellFormatException {
		var rowContent = row.getCell(4).getStringCellValue().strip();
		if (rowContent.matches("")) {
			return obtenerOrigenNota(row);
		} else if (rowContent.matches("^\\d{1,2} *\\(A[a-z]+\\)$")) {
			return CourseStatus.APROBADA;
		} else if (rowContent.matches("^A[a-z]+ *\\(A[a-z]+\\)$")) {
			return CourseStatus.REGULARIZADA;
		} else {
			throw new CellFormatException(LangResource.getString("EstadoMateriaInvalido"));
		}
	}

	private CourseStatus obtenerOrigenNota(Row row) {
		var rowContent = row.getCell(5).getStringCellValue().strip();
		if (rowContent.equals(LangResource.getString("EstadoEnCurso")))
			return CourseStatus.ENROLLED;

		if (rowContent.equals(LangResource.getString("EstadoEquivalencia")))
			return CourseStatus.EQUIVALENCIA;

		return CourseStatus.NO_CURSADA;
	}

	private int parseCourseGrade(String rowContent) {
		return Integer.parseInt(rowContent.split(" *\\([Aa-z]+\\) *$")[0]);
	}

	protected abstract CourseType parseCourseType(Row row) throws CellFormatException;

	private double parseCourseCredits(String rowContent) {
		rowContent = rowContent.strip();
		if (rowContent.matches("^\\d+(?:\\.\\d+)?$"))
			return Double.parseDouble(rowContent);

		return 0;
	}

}
