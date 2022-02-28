package com.organizadorcarrera.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

import com.organizadorcarrera.enumerados.ConfigurationType;

@Entity
@Table(name="CONFIG", schema="LISTADO")
public class Configuration {

	@Id
	@Enumerated(EnumType.STRING)
	private ConfigurationType configurationType;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CONFIGURATION", schema = "LISTADO", joinColumns = {
			@JoinColumn(name = "tipo", referencedColumnName = "configurationType") })
	@MapKeyColumn(name = "parametro")
	@Column(name = "valor")
	private Map<String, String> config;

	public Configuration() { /* JPA exclusive */ }
	
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

	public Set<String> getConfigurationSet() {
		return new HashSet<>(this.config.values());
	}

	public boolean isValid() {
		return !this.config.isEmpty();
	}
}
