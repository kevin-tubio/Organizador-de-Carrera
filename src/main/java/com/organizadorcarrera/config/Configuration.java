package com.organizadorcarrera.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.organizadorcarrera.entity.ConfigurationItem;
import com.organizadorcarrera.enumerados.TipoConfiguracion;

public class Configuration {

	private Map<String, ConfigurationItem> config;

	private TipoConfiguracion tipo;
	
	public Configuration(TipoConfiguracion tipo) {
		this.tipo = tipo;
		this.config = new HashMap<>();
	}

	public <T> void agregar(String clave, T valor) {
		this.config.put(clave, new ConfigurationItem(clave, String.valueOf(valor), this.tipo));
	}

	public String recuperar(String clave) {
		return this.config.get(clave).getValor();
	}

	public TipoConfiguracion getConfigType() {
		return this.tipo;
	}

	public Set<ConfigurationItem> getConfigurations() {
		return new HashSet<>(this.config.values());
	}

	public boolean estaVacia() {
		return this.config.isEmpty();
	}

}
