package com.organizadorcarrera.repository;

import org.springframework.data.repository.CrudRepository;

import com.organizadorcarrera.config.Configuration;
import com.organizadorcarrera.enumerados.ConfigurationType;

public interface ConfigurationRepository extends CrudRepository<Configuration, ConfigurationType> {

	public Configuration findByConfigurationType(ConfigurationType configurationType);

}
