package sistema;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import excepciones.FormatoDeCeldaException;
import excepciones.PlanillaInvalidaException;
import listado.Listado;
import listado.Materia;
import listado.Materia.Estado;

public class InterpretadorDeArchivos {

	public Listado generarListado(String ruta) throws IOException, PlanillaInvalidaException {
		var listado = Listado.obtenerListado();
		var hoja = obtenerHoja(ruta);

		agregarMaterias(listado, hoja);
		return listado;
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
			if (filaActual.getCell(0).getStringCellValue().matches("^[A-Za-zÀ-ÿ]+[A-Za-zÀ-ÿ,. ]+ \\(\\d+\\)")) {
				try {
					listado.agregarMateria(crearMateria(filaActual));
				} catch (FormatoDeCeldaException e) {
					System.err.println("Fila " + (filaActual.getRowNum() + 1) + ", " + e.getMessage());
				}
			}
		}
		if (listado.consultarCantidadDeMaterias() == 0) {
			throw new PlanillaInvalidaException("No se ha podido interpretar las materias de la planilla provista");
		}
	}

	private Materia crearMateria(Row filaActual) throws FormatoDeCeldaException {
		var numero = obtenerNumeroMateria(filaActual.getCell(0).getStringCellValue());
		var nombre = obtenerNombreMateria(filaActual.getCell(0).getStringCellValue());
		var anio = obtenerAnioDeMateria(filaActual.getCell(2).getStringCellValue());
		var periodo = obtenerPeriodoDeMateria(filaActual.getCell(3).getStringCellValue());
		var materia = new Materia(numero, nombre, anio, periodo);
		obtenerEstadoDeMateria(materia, filaActual.getCell(4).getStringCellValue());
		return materia;
	}

	private int obtenerNumeroMateria(String contenido) {
		return Integer.parseInt(contenido.split("[^\\d]+")[1]);
	}

	private String obtenerNombreMateria(String contenido) {
		return contenido.split("[\\d\\(\\)\\n]+")[0].strip();
	}

	private int obtenerAnioDeMateria(String contenido) throws FormatoDeCeldaException {
		try {
			return Integer.parseInt(contenido);
		} catch (NumberFormatException e) {
			throw new FormatoDeCeldaException("columna 3. Se esperaba un numero.", e.getCause());
		}
	}

	private int obtenerPeriodoDeMateria(String contenido) throws FormatoDeCeldaException {
		try {
			return Integer.parseInt(contenido.split("[^\\d]")[0]);
		} catch (NumberFormatException e) {
			throw new FormatoDeCeldaException("columna 4. Se esperaba un numero.", e.getCause());
		}
	}

	private void obtenerEstadoDeMateria(Materia materia, String contenido) throws FormatoDeCeldaException {
		if (contenido.equals("") || contenido.matches("[\s]+")) {
			materia.setEstado(Estado.NO_CURSADA);
		} else if (contenido.matches("^\\d{1,2} \\([A][a-z]+\\)")) {
			materia.setCalificacion(Integer.parseInt(contenido.split(" \\([Aa-z]+\\)")[0]));
			materia.setEstado(Estado.APROBADA);
		} else if (contenido.matches("^[A][a-z]+ \\([A][a-z]+\\)")) {
			materia.setEstado(Estado.REGULARIZADA);
		} else {
			throw new FormatoDeCeldaException("columna 5. Se esperaba un estado de cursada valido o una celda vacia.");
		}
	}

}
