package com.organizadorcarrera.repository;

import org.springframework.data.repository.CrudRepository;

import com.organizadorcarrera.model.Configuration;
import com.organizadorcarrera.enums.ConfigurationType;

public interface ConfigurationRepository extends CrudRepository<Configuration, ConfigurationType> {

	public Configuration findByConfigurationType(ConfigurationType configurationType);

}
