package sistema;

import org.apache.poi.ss.usermodel.Row;

import enumerados.Tipo;
import excepciones.FormatoDeCeldaException;
import listado.Materia;
import util.LangResource;

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
		contenido = contenido.strip();
		if (contenido.equals(LangResource.getString("TipoSeminarioOptativo")))
			return Tipo.SEMINARIO_OPTATIVO;

		if (contenido.equals(LangResource.getString("TipoSeminarioElectivo")))
			return Tipo.SEMINARIO_ELECTIVO;

		if (contenido.equals(LangResource.getString("TipoAsignaturaElectiva")))
			return Tipo.ASIGNATURA_ELECTIVA;

		throw new FormatoDeCeldaException(LangResource.getString("TipoMateriaInvalida"));
	}

}
