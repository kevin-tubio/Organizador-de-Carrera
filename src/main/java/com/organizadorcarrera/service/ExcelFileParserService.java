package com.organizadorcarrera.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.organizadorcarrera.builder.ForeignLanguageCourseParser;
import com.organizadorcarrera.builder.SimpleCourseParser;
import com.organizadorcarrera.builder.SeminaryCourseParser;
import com.organizadorcarrera.exception.FileException;
import com.organizadorcarrera.exception.CellFormatException;
import com.organizadorcarrera.exception.PlanillaInvalidaException;
import com.organizadorcarrera.util.LangResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelFileParserService implements FileParserService {

	private final Logger logger;
	private final SimpleCourseParser simpleCourseParser;
	private final SeminaryCourseParser seminaryCourseParser;
	private final ForeignLanguageCourseParser foreignLanguageCourseParser;
	private final ProgramService programService;

	@Autowired
	public ExcelFileParserService(SimpleCourseParser simpleCourseParser, SeminaryCourseParser seminaryCourseParser, ForeignLanguageCourseParser foreignLanguageCourseParser, ProgramService programService) {
		this.logger = LoggerFactory.getLogger(ExcelFileParserService.class);
		this.simpleCourseParser = simpleCourseParser;
		this.seminaryCourseParser = seminaryCourseParser;
		this.foreignLanguageCourseParser = foreignLanguageCourseParser;
		this.programService = programService;
	}

	@Override
	public void generarListado(String ruta) throws FileException {
		try {
			var hoja = obtenerHoja(ruta);
			agregarMaterias(programService, hoja);
		} catch (FileNotFoundException e) {
			throw new FileException(LangResource.getString("ArchivoNoEncontrado") + ruta);
		} catch (IOException | PlanillaInvalidaException e) {
			programService.clearProgram();
			throw new FileException(e.getMessage(), e.getCause());
		}
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

	private void agregarMaterias(ProgramService listado, Sheet hoja) throws PlanillaInvalidaException {
		for (Row filaActual : hoja) {
			var contenido = filaActual.getCell(0).getStringCellValue().strip();
			try {
				if (contenido.matches("^[A-Za-zÀ-ÿ´]+[A-Za-zÀ-ÿ,. ]+ \\(\\d+\\)$")) {
					listado.addCourse(simpleCourseParser.crearMateria(filaActual));
				} else if (contenido.matches("^[A-Za-z ]+: \"[A-Za-zÀ-ÿ´ ]+\" \\(\\d+\\)$")) {
					listado.addCourse(seminaryCourseParser.crearMateria(filaActual));
				} else if (contenido.matches("^[A-Za-z ]+ \\([A-Za-zÀ-ÿ ]+\\) \\(\\d+\\)$")) {
					listado.addCourse(foreignLanguageCourseParser.crearMateria(filaActual));
				}
			} catch (CellFormatException e) {
				logger.error(LangResource.getString("FilaInvalida"), (filaActual.getRowNum() + 1), e.getMessage());
			}
		}
		if (listado.isEmpty()) {
			throw new PlanillaInvalidaException(LangResource.getString("NoEsPosibleInterpretarPlanilla"));
		}
	}

}
