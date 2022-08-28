package com.organizadorcarrera.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.organizadorcarrera.model.Configuration;
import com.organizadorcarrera.enums.ConfigurationType;
import com.organizadorcarrera.repository.ConfigurationRepository;

@Service
public class ConfigurationService {

	private final ConfigurationRepository configurationRepository;

	@Autowired
	public ConfigurationService(ConfigurationRepository configurationRepository) {
		 this.configurationRepository = configurationRepository;
	}
	
	public Configuration findByTipoConfiguracion(ConfigurationType configurationType) {
		return configurationRepository.findByConfigurationType(configurationType);
	}
	
	public void save(Configuration configuration) {
		configurationRepository.save(configuration);
	}
}
