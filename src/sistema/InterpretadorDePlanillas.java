package sistema;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import enumerados.Estado;
import enumerados.Periodo;
import excepciones.ArchivoException;
import excepciones.FormatoDeCeldaException;
import excepciones.PlanillaInvalidaException;
import listado.Listado;
import listado.Materia;

public class InterpretadorDePlanillas implements InterpretadorDeArchivos {

	@Override
	public Listado generarListado(String ruta) throws ArchivoException {
		var listado = Listado.obtenerListado();
		try {
			var hoja = obtenerHoja(ruta);
			agregarMaterias(listado, hoja);
		} catch (FileNotFoundException e) {
			throw new ArchivoException("No se encontro el archivo en: " + ruta);
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
					listado.agregarMateria(crearMateria(filaActual));
				}
			} catch (FormatoDeCeldaException e) {
				System.err.println("Fila " + (filaActual.getRowNum() + 1) + ", " + e.getMessage());
			}
		}
		if (listado.consultarCantidadDeMaterias() == 0) {
			throw new PlanillaInvalidaException("No se ha podido interpretar las materias de la planilla provista");
		}
	}

	private Materia crearMateria(Row filaActual) throws FormatoDeCeldaException {
		var nombre = obtenerNombreMateria(filaActual.getCell(0).getStringCellValue());
		return generarMateria(nombre, filaActual);
	}

	private Materia generarMateria(String nombre, Row filaActual) throws FormatoDeCeldaException {
		var id = obtenerNumeroMateria(filaActual.getCell(0).getStringCellValue());
		var anio = obtenerAnioDeMateria(filaActual.getCell(2));
		var periodo = obtenerPeriodoDeMateria(filaActual.getCell(3).getStringCellValue());
		var materia = new Materia(id, nombre, anio, periodo);
		materia.setEstado(obtenerEstadoDeMateria(filaActual));
		if (materia.getEstado().equals(Estado.APROBADA))
			materia.setCalificacion(obtenerNotaDeMateria(filaActual.getCell(4).getStringCellValue()));
		return materia;
	}

	private int obtenerNumeroMateria(String contenido) {
		return Integer.parseInt(contenido.split("[^\\d]+")[1]);
	}

	private String obtenerNombreMateria(String contenido) {
		return contenido.split(" ?\\([\\d]+\\)")[0];
	}

	private int obtenerAnioDeMateria(Cell celda) throws FormatoDeCeldaException {
		try {
			return Integer.parseInt(celda.getStringCellValue().strip());
		} catch (NumberFormatException e) {
			throw new FormatoDeCeldaException("columna 3. Se esperaba un numero.", e.getCause());
		} catch (IllegalStateException e) {
			return (int) celda.getNumericCellValue();
		}
	}

	private Periodo obtenerPeriodoDeMateria(String contenido) throws FormatoDeCeldaException {
		if (!contenido.matches("^([1-2A-Z][a-z]+[ ]+){0,1}[A-Z][a-z]+[ ]*$")) {
			throw new FormatoDeCeldaException("columna 4. Formato invalido");
		}
		switch (contenido.strip()) {
		case "1er Cuatrimestre":
			return Periodo.PRIMER_CUATRIMESTRE;
		case "2do Cuatrimestre":
			return Periodo.SEGUNDO_CUATRIMESTRE;
		case "Anual":
			return Periodo.ANUAL;
		default:
			return Periodo.ANUAL;
		}
	}

	private int obtenerNotaDeMateria(String contenido) {
		return Integer.parseInt(contenido.split(" *\\([Aa-z]+\\) *$")[0]);
	}

	private Estado obtenerEstadoDeMateria(Row fila) throws FormatoDeCeldaException {
		var contenido = fila.getCell(4).getStringCellValue().strip();
		if (contenido.matches("")) {
			return (fila.getCell(5).getStringCellValue().equals("En Curso") ? Estado.EN_CURSO : Estado.NO_CURSADA);
		} else if (contenido.matches("^\\d{1,2} *\\([A][a-z]+\\)$")) {
			return Estado.APROBADA;
		} else if (contenido.matches("^[A][a-z]+ *\\([A][a-z]+\\)$")) {
			return Estado.REGULARIZADA;
		} else {
			throw new FormatoDeCeldaException("columna 5. Se esperaba un estado de cursada valido o una celda vacia.");
		}
	}

}
