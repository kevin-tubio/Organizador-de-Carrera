package com.organizadorcarrera.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.organizadorcarrera.builder.ForeignLanguageCourseBuilder;
import com.organizadorcarrera.builder.SimpleCourseBuilder;
import com.organizadorcarrera.builder.SeminaryCourseBuilder;
import com.organizadorcarrera.exception.FileException;
import com.organizadorcarrera.exception.CellFormatException;
import com.organizadorcarrera.exception.PlanillaInvalidaException;
import com.organizadorcarrera.program.Program;
import com.organizadorcarrera.util.LangResource;

public class ExcelFileParser implements FileParser {

	private Logger logger;

	public ExcelFileParser() {
		this.logger = LoggerFactory.getLogger(ExcelFileParser.class);
	}

	@Override
	public Program generarListado(String ruta) throws FileException {
		var listado = Program.getInstance();
		try {
			var hoja = obtenerHoja(ruta);
			agregarMaterias(listado, hoja);
		} catch (FileNotFoundException e) {
			throw new FileException(LangResource.getString("ArchivoNoEncontrado") + ruta);
		} catch (IOException | PlanillaInvalidaException e) {
			Program.clearProgram();
			throw new FileException(e.getMessage(), e.getCause());
		}
		return Program.getInstance();
	}

	private Sheet obtenerHoja(String ruta) throws IOException {
		try (var libro = new HSSFWorkbook(new FileInputStream(ruta))) {
			return libro.getSheetAt(0);
		} catch (OfficeXmlFileException e) {
			try (var libro = new XSSFWorkbook(new FileInputStream(ruta))) {
				return libro.getSheetAt(0);
			}
		}
	}

	private void agregarMaterias(Program listado, Sheet hoja) throws PlanillaInvalidaException {
		Iterator<Row> iteradorFilas = hoja.iterator();
		while (iteradorFilas.hasNext()) {
			Row filaActual = iteradorFilas.next();
			var contenido = filaActual.getCell(0).getStringCellValue().strip();
			try {
				if (contenido.matches("^[A-Za-zÀ-ÿ´]+[A-Za-zÀ-ÿ,. ]+ \\(\\d+\\)$")) {
					listado.addCourse(new SimpleCourseBuilder().crearMateria(filaActual));
				} else if (contenido.matches("^[A-Za-z ]+: \"[A-Za-zÀ-ÿ´ ]+\" \\(\\d+\\)$")) {
					listado.addCourse(new SeminaryCourseBuilder().crearMateria(filaActual));
				} else if (contenido.matches("^[A-Za-z ]+ \\([A-Za-zÀ-ÿ ]+\\) \\(\\d+\\)$")) {
					listado.addCourse(new ForeignLanguageCourseBuilder().crearMateria(filaActual));
				}
			} catch (CellFormatException e) {
				logger.error(LangResource.getString("FilaInvalida"), (filaActual.getRowNum() + 1), e.getMessage());
			}
		}
		if (listado.getCoursesCount() == 0) {
			throw new PlanillaInvalidaException(LangResource.getString("NoEsPosibleInterpretarPlanilla"));
		}
	}

}
