package com.organizadorcarrera.repository;

import org.springframework.data.repository.CrudRepository;

import com.organizadorcarrera.config.Configuration;
import com.organizadorcarrera.enumerados.TipoConfiguracion;

public interface ConfigurationRepository extends CrudRepository<Configuration, TipoConfiguracion> {

	public Configuration findByTipoConfiguracion(TipoConfiguracion tipoConfiguracion);

}
