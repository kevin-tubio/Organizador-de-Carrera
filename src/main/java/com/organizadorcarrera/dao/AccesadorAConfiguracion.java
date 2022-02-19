package com.organizadorcarrera.dao;

import com.organizadorcarrera.config.Configuration;
import com.organizadorcarrera.enumerados.TipoConfiguracion;

public class AccesadorAConfiguracion extends AccesadorADatos<Configuration> {

	public AccesadorAConfiguracion() {
		super(Configuration.class);
	}

	public void persistirConfiguracion(Configuration config) {
		super.ejecutarTransaccion(entityManager -> entityManager.persist(config));
	}

	public void actualizarConfiguracion(Configuration config) {
		super.ejecutarTransaccion(entityManager -> entityManager.merge(config));
	}

	public Configuration obtenerConfiguracion(TipoConfiguracion tipo) {
		super.getManager().find(Configuration.class.getClass(), tipo.toString());
		return new Configuration();
	}

}
