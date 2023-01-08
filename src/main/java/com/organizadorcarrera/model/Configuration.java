package com.organizadorcarrera.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.organizadorcarrera.enums.ConfigurationType;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CONFIGURATION_TYPE")
@NoArgsConstructor
public class Configuration {

	@Id
	@Enumerated(EnumType.STRING)
	private ConfigurationType configurationType;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CONFIGURATION", joinColumns = {
			@JoinColumn(name = "type", referencedColumnName = "configurationType") })
	@MapKeyColumn(name = "parameter")
	@Column(name = "value")
	private Map<String, String> config;
	
	public Configuration(ConfigurationType configurationType) {
		this.configurationType = configurationType;
		this.config = new HashMap<>();
	}

	public <T> void addConfigurationPair(String key, T value) {
		this.config.put(key, String.valueOf(value));
	}

	public String getConfigurationValue(String key) {
		return this.config.get(key);
	}

	public ConfigurationType getConfigurationType() {
		return this.configurationType;
	}

	public boolean isEmpty() {
		return this.config.isEmpty();
	}

}
