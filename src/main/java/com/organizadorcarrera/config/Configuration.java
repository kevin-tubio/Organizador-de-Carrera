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

import com.organizadorcarrera.enumerados.TipoConfiguracion;

@Entity
@Table(name="CONFIG", schema="LISTADO")
public class Configuration {

	@Id
	@Enumerated(EnumType.STRING)
	private TipoConfiguracion tipoConfiguracion;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CONFIGURATION", schema = "LISTADO", joinColumns = {
			@JoinColumn(name = "tipo", referencedColumnName = "tipoConfiguracion") })
	@MapKeyColumn(name = "parametro")
	@Column(name = "valor")
	private Map<String, String> config;

	public Configuration() { /* JPA exclusive */ }
	
	public Configuration(TipoConfiguracion tipoConfiguracion) {
		this.tipoConfiguracion = tipoConfiguracion;
		this.config = new HashMap<>();
	}

	public <T> void agregar(String clave, T valor) {
		this.config.put(clave, String.valueOf(valor));
	}

	public String recuperar(String clave) {
		return this.config.get(clave);
	}

	public TipoConfiguracion getConfigType() {
		return this.tipoConfiguracion;
	}

	public Set<String> getConfigurations() {
		return new HashSet<>(this.config.values());
	}

	public boolean estaVacia() {
		return this.config.isEmpty();
	}
}
