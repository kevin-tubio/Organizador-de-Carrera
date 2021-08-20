package sistema;

import enumerados.Tipo;
import excepciones.FormatoDeCeldaException;

public class CreadorDeIdiomaExtranjero extends CreadorDeMateria {

	@Override
	protected String obtenerNombreMateria(String contenido) {
		contenido = super.obtenerNombreMateria(contenido);
		contenido = contenido.split("\\(")[1];
		return contenido.replace(")", "");
	}

	@Override
	protected Tipo obtenerTipoDeMateria(String contenido) throws FormatoDeCeldaException {
		return Tipo.IDIOMA_EXTRANJERO;
	}

}
