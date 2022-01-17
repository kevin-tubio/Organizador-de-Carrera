package sistema;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import entity.Materia;
import enumerados.Estado;
import enumerados.Periodo;
import enumerados.Tipo;
import excepciones.FormatoDeCeldaException;
import util.LangResource;

public abstract class CreadorDeMateria {

	public Materia crearMateria(Row filaActual) throws FormatoDeCeldaException {
		var nombre = obtenerNombreMateria(filaActual.getCell(0).getStringCellValue());
		var materia = generarMateria(nombre, filaActual);
		materia.setTipo(obtenerTipoDeMateria(filaActual.getCell(1).getStringCellValue()));
		return materia;
	}

	protected Materia generarMateria(String nombre, Row filaActual) throws FormatoDeCeldaException {
		var id = obtenerNumeroMateria(filaActual.getCell(0).getStringCellValue());
		var anio = obtenerAnioDeMateria(filaActual.getCell(2));
		var periodo = obtenerPeriodoDeMateria(filaActual.getCell(3).getStringCellValue());
		var materia = new Materia(id, nombre, anio, periodo);
		materia.setEstado(obtenerEstadoDeMateria(filaActual));
		if (materia.getEstado().equals(Estado.APROBADA))
			materia.setCalificacion(obtenerNotaDeMateria(filaActual.getCell(4).getStringCellValue()));
		materia.setCreditos(obtenerCreditosDeMateria(filaActual.getCell(6).getStringCellValue()));
		return materia;
	}

	private int obtenerNumeroMateria(String contenido) {
		return Integer.parseInt(contenido.split("[^\\d]+")[1]);
	}

	protected String obtenerNombreMateria(String contenido) {
		return contenido.split(" ?\\([\\d]+\\)")[0];
	}

	private int obtenerAnioDeMateria(Cell celda) throws FormatoDeCeldaException {
		try {
			return Integer.parseInt(celda.getStringCellValue().strip());
		} catch (NumberFormatException e) {
			throw new FormatoDeCeldaException(LangResource.getString("NumeroEsperadoColumna"), e.getCause());
		} catch (IllegalStateException e) {
			return (int) celda.getNumericCellValue();
		}
	}

	private Periodo obtenerPeriodoDeMateria(String contenido) throws FormatoDeCeldaException {
		if (!contenido.matches("^([1-2A-Z][a-z]+[ ]+){0,1}[A-Z][a-z]+[ ]*$")) {
			throw new FormatoDeCeldaException(LangResource.getString("FormatoInvalido"));
		}
		switch (contenido.replaceAll("[ ]+", " ").strip()) {
		case "1er Cuatrimestre":
			return Periodo.PRIMER_CUATRIMESTRE;
		case "2do Cuatrimestre":
			return Periodo.SEGUNDO_CUATRIMESTRE;
		default:
			return Periodo.ANUAL;
		}
	}

	private Estado obtenerEstadoDeMateria(Row fila) throws FormatoDeCeldaException {
		var contenido = fila.getCell(4).getStringCellValue().strip();
		if (contenido.matches("")) {
			return obtenerOrigenNota(fila);
		} else if (contenido.matches("^\\d{1,2} *\\([A][a-z]+\\)$")) {
			return Estado.APROBADA;
		} else if (contenido.matches("^[A][a-z]+ *\\([A][a-z]+\\)$")) {
			return Estado.REGULARIZADA;
		} else {
			throw new FormatoDeCeldaException(LangResource.getString("EstadoMateriaInvalido"));
		}
	}

	private Estado obtenerOrigenNota(Row fila) {
		var contenido = fila.getCell(5).getStringCellValue().strip();
		if (contenido.equals(LangResource.getString("EstadoEnCurso")))
			return Estado.EN_CURSO;

		if (contenido.equals(LangResource.getString("EstadoEquivalencia")))
			return Estado.EQUIVALENCIA;

		return Estado.NO_CURSADA;
	}

	private int obtenerNotaDeMateria(String contenido) {
		return Integer.parseInt(contenido.split(" *\\([Aa-z]+\\) *$")[0]);
	}

	protected abstract Tipo obtenerTipoDeMateria(String contenido) throws FormatoDeCeldaException;

	private double obtenerCreditosDeMateria(String contenido) {
		contenido = contenido.strip();
		if (contenido.matches("^\\d+(?:\\.\\d+)?$"))
			return Double.parseDouble(contenido);

		return 0;
	}

}
