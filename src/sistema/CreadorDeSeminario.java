package sistema;

import org.apache.poi.ss.usermodel.Row;

import enumerados.Tipo;
import excepciones.FormatoDeCeldaException;
import listado.Materia;

public class CreadorDeSeminario extends CreadorDeMateria {

	@Override
	public Materia crearMateria(Row filaActual) throws FormatoDeCeldaException {
		var nombre = obtenerNombreMateria(filaActual.getCell(0).getStringCellValue());
		var materia = super.generarMateria(nombre, filaActual);
		materia.setTipo(obtenerTipoDeMateria(filaActual.getCell(0).getStringCellValue().split(":")[0]));
		return materia;
	}

	@Override
	protected String obtenerNombreMateria(String contenido) {
		contenido = super.obtenerNombreMateria(contenido);
		contenido = contenido.split(":")[1].strip();
		return contenido.replace("\"", "");
	}

	@Override
	protected Tipo obtenerTipoDeMateria(String contenido) throws FormatoDeCeldaException {
		switch (contenido.strip()) {
		case "Seminario optativo":
			return Tipo.SEMINARIO_OPTATIVO;
		case "Seminario electivo":
			return Tipo.SEMINARIO_ELECTIVO;
		case "Asignatura Electiva":
			return Tipo.ASIGNATURA_ELECTIVA;
		default:
			throw new FormatoDeCeldaException("columna 2. Se esperaba un tipo de materia valido.");
		}
	}

}
