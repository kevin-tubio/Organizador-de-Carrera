package com.organizadorcarrera.repository;

import org.springframework.data.repository.CrudRepository;

import com.organizadorcarrera.model.Configuration;
import com.organizadorcarrera.enums.ConfigurationType;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration, ConfigurationType> {

	Configuration findByConfigurationType(ConfigurationType configurationType);

}
