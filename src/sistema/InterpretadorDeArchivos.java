package sistema;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import excepciones.PlanillaInvalidaException;
import listado.Listado;
import listado.Materia;

public class InterpretadorDeArchivos {

	public Listado generarListado(String ruta) throws IOException, PlanillaInvalidaException {
		var listado = Listado.obtenerListado();
		Sheet hoja = obtenerSheet(ruta);
		validarPlanilla(hoja);
		agregarMaterias(listado, hoja);
		return listado;
	}

	private void validarPlanilla(Sheet hoja) {
		
	}

	private Sheet obtenerSheet(String ruta) throws IOException {
		try (var libro = new HSSFWorkbook(new FileInputStream(ruta))) {
			HSSFSheet hoja = libro.getSheetAt(0);
			return hoja;
		} catch(OfficeXmlFileException e) {
			try (var libro = new XSSFWorkbook(new FileInputStream(ruta))) {
				XSSFSheet hoja = libro.getSheetAt(0);
				return hoja;
			}
		}
	}

	private void agregarMaterias(Listado listado, Sheet hoja) {
		Iterator<Row> iteradorFilas = hoja.iterator();
		Iterator<Cell> iteradorColumnas = null;
		while (iteradorFilas.hasNext()) {
			Row filaActual = iteradorFilas.next();
			if (filaActual.getLastCellNum() == 1 || filaActual.getCell(0).getStringCellValue().equals("Actividad"))
				continue;

			iteradorColumnas = filaActual.cellIterator();
			ArrayDeque<String> colaDeDatos = new ArrayDeque<>();
			while (iteradorColumnas.hasNext()) {
				colaDeDatos.offer(iteradorColumnas.next().getStringCellValue());
			}

			listado.agregarMateria(crearMateria(colaDeDatos));
		}
	}

	public Materia crearMateria(Deque<String> colaDeDatos) {
		String cadena = colaDeDatos.peek();
		var numeroActividad = Integer.parseInt(cadena.split("[^\\d]+")[1]);
		String nombre = colaDeDatos.poll().split("[\\d\\(\\)\\n]+")[0].strip();
		colaDeDatos.poll();
		var anio = Integer.parseInt(colaDeDatos.poll());
		var periodo = Integer.parseInt(colaDeDatos.poll().split("[^\\d]")[0]);

		return new Materia(numeroActividad, nombre, anio, periodo);
	}

}
