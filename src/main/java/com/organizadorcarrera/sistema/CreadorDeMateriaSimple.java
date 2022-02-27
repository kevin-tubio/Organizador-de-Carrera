package com.organizadorcarrera.sistema;

import com.organizadorcarrera.enumerados.Tipo;
import com.organizadorcarrera.exception.FormatoDeCeldaException;
import com.organizadorcarrera.util.LangResource;

public class CreadorDeMateriaSimple extends CreadorDeMateria {

	@Override
	protected String obtenerNombreMateria(String contenido) {
		return contenido.split(" ?\\([\\d]+\\)")[0];
	}

	@Override
	protected Tipo obtenerTipoDeMateria(String contenido) throws FormatoDeCeldaException {
		contenido = contenido.strip();
		if (contenido.equals(LangResource.getString("TipoMateria")))
			return Tipo.MATERIA;

		if (contenido.equals(LangResource.getString("TipoTesis")))
			return Tipo.TESIS;

		throw new FormatoDeCeldaException(LangResource.getString("TipoMateriaInvalida"));
	}

}
