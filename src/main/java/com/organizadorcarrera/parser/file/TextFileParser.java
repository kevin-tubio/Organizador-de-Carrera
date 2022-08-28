package com.organizadorcarrera.parser.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.organizadorcarrera.service.ProgramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.enums.CourseStatus;
import com.organizadorcarrera.enums.CoursePeriod;
import com.organizadorcarrera.exception.FileException;
import com.organizadorcarrera.exception.LineFormatException;
import com.organizadorcarrera.exception.InvalidCourseException;
import com.organizadorcarrera.util.LangResource;

import org.springframework.stereotype.Component;

@Component
public class TextFileParser implements FileParser {

	private int numeroDeLinea;
	private final Logger logger;
	private final ProgramService programService;

	public TextFileParser(ProgramService programService) {
		this.logger = LoggerFactory.getLogger(TextFileParser.class);
		this.programService = programService;
	}

	@Override
	public void generarListado(String ruta) throws FileException {
		numeroDeLinea = 0;
		try (var buffer = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), StandardCharsets.UTF_8))) {
			agregarMaterias(buffer);
			obtenerCorrelativas(buffer);
		} catch (FileNotFoundException e) {
			throw new FileException(LangResource.getString("ArchivoNoEncontrado") + ruta);
		} catch (IOException e) {
			programService.clearProgram();
			throw new FileException(e.getMessage(), e.getCause());
		}
	}

	private String validarLinea(String lineaActual, String mensaje) throws LineFormatException {
		if (lineaActual == null || lineaActual.isBlank()) {
			throw new LineFormatException(mensaje);
		}
		return lineaActual.strip();
	}

	private void agregarMaterias(BufferedReader buffer) throws IOException, FileException {
		var cantidadDeMaterias = obtenerCantidadDeMaterias(buffer, LangResource.getString("NumeroEsperadoLinea"));
		for (var i = 0; i < cantidadDeMaterias; i++) {
			try {
				programService.addCourse(crearMateria(buffer));
			} catch (LineFormatException e) {
				logger.error(LangResource.getString("LineaInvalida"), this.numeroDeLinea, e.getMessage());
			}
		}
	}

	private int obtenerCantidadDeMaterias(BufferedReader buffer, String alerta) throws IOException, FileException {
		try {
			numeroDeLinea++;
			return Integer.parseInt(validarLinea(buffer.readLine(), alerta));
		} catch (NumberFormatException e) {
			throw new LineFormatException(alerta);
		}
	}

	private Course crearMateria(BufferedReader buffer) throws IOException, FileException {
		numeroDeLinea++;
		var linea = validarLinea(buffer.readLine(), LangResource.getString("DatosMateriaEsperados"));
		if (!linea.matches("^\\d+/[A-Za-zÀ-ÿ,. ]+/\\d/([1-2A-Z][a-z]+ +)?[A-Z][a-z]+/\\d{1,2}/[A-Za-z ]*$")) {
			throw new LineFormatException(formatearMensajeExcepcion(LangResource.getString("FormatoInvalido")));
		}
		String[] datos = linea.split("/");
		var numeroDeMateria = obtenerNumero(datos[0]);
		var nombre = datos[1];
		var anio = obtenerNumero(datos[2]);
		var periodo = obtenerPeriodo(datos[3]);
		var materia = new Course(numeroDeMateria, nombre, anio, periodo);
		materia.setGrade(obtenerNumero(datos[4]));
		materia.setCourseStatus(obtenerEstadoDeMateria(datos[5]));
		return materia;
	}

	private CoursePeriod obtenerPeriodo(String dato) {
		dato = dato.replaceAll(" +", " ");
		if (dato.equals(LangResource.getString("PeriodoPrimerCuatrimestre")))
			return CoursePeriod.PRIMER_CUATRIMESTRE;

		if (dato.equals(LangResource.getString("PeriodoSegundoCuatrimestre")))
			return CoursePeriod.SEGUNDO_CUATRIMESTRE;

		return CoursePeriod.ANUAL;
	}

	private CourseStatus obtenerEstadoDeMateria(String dato) {
		if (dato.equals(LangResource.getString("EstadoAprobado")))
			return CourseStatus.APROBADA;

		if (dato.equals(LangResource.getString("EstadoRegularizado")))
			return CourseStatus.REGULARIZADA;

		return CourseStatus.NO_CURSADA;
	}

	private int obtenerNumero(String dato) {
		return Integer.parseInt(dato);
	}

	private void obtenerCorrelativas(BufferedReader buffer) throws IOException {
		numeroDeLinea++;
		var linea = buffer.readLine();

		if (linea == null || !linea.strip().matches("\\d+"))
			return;

		var cantidad = Integer.parseInt(linea);
		for (var i = 0; i < cantidad; i++) {
			agregarCorrelativa(buffer);
		}
	}

	private void agregarCorrelativa(BufferedReader buffer) throws IOException {
		numeroDeLinea++;
		var linea = buffer.readLine();

		if (linea == null || !linea.strip().matches("^\\d+: *[\\d+/]+ *$")) {
			logger.warn(LangResource.getString("LineaInvalida"), this.numeroDeLinea, LangResource.getString("IdCorrelativasEsperado"));
			return;
		}

		String[] datos = linea.split(": *");
		try {
			String[] correlativas = datos[1].split("/");
			var numeros = new int[correlativas.length];
			for (var i = 0; i < numeros.length; i++) {
				numeros[i] = Integer.parseInt(correlativas[i].strip());
			}
			programService.addCorrelatives(Integer.parseInt(datos[0]), numeros);
		} catch (InvalidCourseException e) {
			logger.error(e.getMessage());
		}
	}

	private String formatearMensajeExcepcion(String mensaje) {
		String mensajeExcepcion = LangResource.getString("LineaInvalida").replaceFirst("\\{\\}", String.valueOf(this.numeroDeLinea));
		return mensajeExcepcion.replace("{}", mensaje);
	}

}
