package com.organizadorcarrera.sistema;

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

import com.organizadorcarrera.exception.ArchivoException;
import com.organizadorcarrera.exception.FormatoDeCeldaException;
import com.organizadorcarrera.exception.PlanillaInvalidaException;
import com.organizadorcarrera.listado.Listado;
import com.organizadorcarrera.util.LangResource;

public class InterpretadorDePlanillas implements InterpretadorDeArchivos {

	private Logger logger;

	public InterpretadorDePlanillas() {
		this.logger = LoggerFactory.getLogger(InterpretadorDePlanillas.class);
	}

	@Override
	public Listado generarListado(String ruta) throws ArchivoException {
		var listado = Listado.obtenerListado();
		try {
			var hoja = obtenerHoja(ruta);
			agregarMaterias(listado, hoja);
		} catch (FileNotFoundException e) {
			throw new ArchivoException(LangResource.getString("ArchivoNoEncontrado") + ruta);
		} catch (IOException | PlanillaInvalidaException e) {
			Listado.borrarListado();
			throw new ArchivoException(e.getMessage(), e.getCause());
		}
		return Listado.obtenerListado();
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

	private void agregarMaterias(Listado listado, Sheet hoja) throws PlanillaInvalidaException {
		Iterator<Row> iteradorFilas = hoja.iterator();
		while (iteradorFilas.hasNext()) {
			Row filaActual = iteradorFilas.next();
			var contenido = filaActual.getCell(0).getStringCellValue().strip();
			try {
				if (contenido.matches("^[A-Za-zÀ-ÿ´]+[A-Za-zÀ-ÿ,. ]+ \\(\\d+\\)$")) {
					listado.agregarMateria(new CreadorDeMateriaSimple().crearMateria(filaActual));
				} else if (contenido.matches("^[A-Za-z ]+: \"[A-Za-zÀ-ÿ´ ]+\" \\(\\d+\\)$")) {
					listado.agregarMateria(new CreadorDeSeminario().crearMateria(filaActual));
				} else if (contenido.matches("^[A-Za-z ]+ \\([A-Za-zÀ-ÿ ]+\\) \\(\\d+\\)$")) {
					listado.agregarMateria(new CreadorDeIdiomaExtranjero().crearMateria(filaActual));
				}
			} catch (FormatoDeCeldaException e) {
				logger.error(LangResource.getString("FilaInvalida"), (filaActual.getRowNum() + 1), e.getMessage());
			}
		}
		if (listado.consultarCantidadDeMaterias() == 0) {
			throw new PlanillaInvalidaException(LangResource.getString("NoEsPosibleInterpretarPlanilla"));
		}
	}

}
