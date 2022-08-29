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

	public Course crearMateria(Row filaActual) throws CellFormatException {
		var nombre = obtenerNombreMateria(filaActual.getCell(0).getStringCellValue());
		var materia = generarMateria(nombre, filaActual);
		materia.setCourseType(obtenerTipoDeMateria(filaActual.getCell(1).getStringCellValue()));
		return materia;
	}

	protected Course generarMateria(String nombre, Row filaActual) throws CellFormatException {
		var id = obtenerNumeroMateria(filaActual.getCell(0).getStringCellValue());
		var anio = obtenerAnioDeMateria(filaActual.getCell(2));
		var periodo = obtenerPeriodoDeMateria(filaActual.getCell(3).getStringCellValue());
		var materia = new Course(id, nombre, anio, periodo);
		materia.setCourseStatus(obtenerEstadoDeMateria(filaActual));
		if (materia.getCourseStatus().equals(CourseStatus.APROBADA))
			materia.setGrade(obtenerNotaDeMateria(filaActual.getCell(4).getStringCellValue()));
		materia.setCreditos(obtenerCreditosDeMateria(filaActual.getCell(6).getStringCellValue()));
		return materia;
	}

	private int obtenerNumeroMateria(String contenido) {
		return Integer.parseInt(contenido.split("\\D+")[1]);
	}

	protected String obtenerNombreMateria(String contenido) {
		return contenido.split(" ?\\(\\d+\\)")[0];
	}

	private int obtenerAnioDeMateria(Cell celda) throws CellFormatException {
		try {
			return Integer.parseInt(celda.getStringCellValue().strip());
		} catch (NumberFormatException e) {
			throw new CellFormatException(LangResource.getString("NumeroEsperadoColumna"), e.getCause());
		} catch (IllegalStateException e) {
			return (int) celda.getNumericCellValue();
		}
	}

	private CoursePeriod obtenerPeriodoDeMateria(String contenido) throws CellFormatException {
		if (!contenido.matches("^([1-2A-Z][a-z]+ +)?[A-Z][a-z]+ *$"))
			throw new CellFormatException(LangResource.getString("FormatoInvalido"));

		contenido = contenido.replaceAll(" +", " ").strip();
		if (contenido.equals(LangResource.getString("PeriodoPrimerCuatrimestre")))
			return CoursePeriod.PRIMER_CUATRIMESTRE;

		if (contenido.equals(LangResource.getString("PeriodoSegundoCuatrimestre")))
			return CoursePeriod.SEGUNDO_CUATRIMESTRE;

		return CoursePeriod.ANUAL;
	}

	private CourseStatus obtenerEstadoDeMateria(Row fila) throws CellFormatException {
		var contenido = fila.getCell(4).getStringCellValue().strip();
		if (contenido.matches("")) {
			return obtenerOrigenNota(fila);
		} else if (contenido.matches("^\\d{1,2} *\\(A[a-z]+\\)$")) {
			return CourseStatus.APROBADA;
		} else if (contenido.matches("^A[a-z]+ *\\(A[a-z]+\\)$")) {
			return CourseStatus.REGULARIZADA;
		} else {
			throw new CellFormatException(LangResource.getString("EstadoMateriaInvalido"));
		}
	}

	private CourseStatus obtenerOrigenNota(Row fila) {
		var contenido = fila.getCell(5).getStringCellValue().strip();
		if (contenido.equals(LangResource.getString("EstadoEnCurso")))
			return CourseStatus.ENROLLED;

		if (contenido.equals(LangResource.getString("EstadoEquivalencia")))
			return CourseStatus.EQUIVALENCIA;

		return CourseStatus.NO_CURSADA;
	}

	private int obtenerNotaDeMateria(String contenido) {
		return Integer.parseInt(contenido.split(" *\\([Aa-z]+\\) *$")[0]);
	}

	protected abstract CourseType obtenerTipoDeMateria(String contenido) throws CellFormatException;

	private double obtenerCreditosDeMateria(String contenido) {
		contenido = contenido.strip();
		if (contenido.matches("^\\d+(?:\\.\\d+)?$"))
			return Double.parseDouble(contenido);

		return 0;
	}

}
