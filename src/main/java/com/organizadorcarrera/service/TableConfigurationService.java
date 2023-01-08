package com.organizadorcarrera.service;

import com.organizadorcarrera.enums.ConfigurationType;
import com.organizadorcarrera.model.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TableConfigurationService {

	private final ConfigurationService configurationService;

	public Configuration getTableConfiguration() {
		Configuration tableConfiguration = configurationService.findByTipoConfiguracion(ConfigurationType.TABLE);
		return isConfigurationValid(tableConfiguration) ? tableConfiguration : new Configuration(ConfigurationType.TABLE);
	}

	public void saveTableConfiguration(Configuration configuration) {
		this.configurationService.save(configuration);
	}

	public boolean isConfigurationValid(Configuration configuration) {
		if (configuration == null || !ConfigurationType.TABLE.equals(configuration.getConfigurationType()))
			return false;

		return !configuration.isEmpty();
	}

}
