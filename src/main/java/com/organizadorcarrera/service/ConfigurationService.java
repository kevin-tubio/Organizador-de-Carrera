package com.organizadorcarrera.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.organizadorcarrera.config.Configuration;
import com.organizadorcarrera.enumerados.TipoConfiguracion;
import com.organizadorcarrera.repository.ConfigurationRepository;

@Service
public class ConfigurationService {

	@Autowired
	private ConfigurationRepository configurationRepository;
	
	public Configuration findByTipoConfiguracion(TipoConfiguracion tipoConfiguracion) {
		return configurationRepository.findByTipoConfiguracion(tipoConfiguracion);
	}
	
	public void save(Configuration configuration) {
		configurationRepository.save(configuration);
	}
}
