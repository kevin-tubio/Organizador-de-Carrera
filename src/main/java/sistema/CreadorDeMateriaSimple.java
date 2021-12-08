package sistema;

import enumerados.Tipo;
import excepciones.FormatoDeCeldaException;

public class CreadorDeMateriaSimple extends CreadorDeMateria {

	@Override
	protected String obtenerNombreMateria(String contenido) {
		return contenido.split(" ?\\([\\d]+\\)")[0];
	}

	@Override
	protected Tipo obtenerTipoDeMateria(String contenido) throws FormatoDeCeldaException {
		switch (contenido.strip()) {
		case "Materia":
			return Tipo.MATERIA;
		case "Tesis":
			return Tipo.TESIS;
		default:
			throw new FormatoDeCeldaException("columna 2. Se esperaba un tipo de materia valido.");
		}
	}

}
