package com.organizadorcarrera.service;

import com.organizadorcarrera.enums.ConfigurationType;
import com.organizadorcarrera.model.Configuration;
import com.organizadorcarrera.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

	private final ConfigurationRepository configurationRepository;

	public Configuration findByTipoConfiguracion(ConfigurationType configurationType) {
		return configurationRepository.findByConfigurationType(configurationType);
	}

	public void save(Configuration configuration) {
		configurationRepository.save(configuration);
	}
}
