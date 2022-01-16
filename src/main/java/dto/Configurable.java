package dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import entity.Configuracion;
import enumerados.TipoConfiguracion;

public class Configurable {

	private Map<String, Configuracion> config;

	private TipoConfiguracion tipo;
	
	public Configurable(TipoConfiguracion tipo) {
		this.tipo = tipo;
		this.config = new HashMap<>();
	}

	public <T> void agregar(String clave, T valor) {
		this.config.put(clave, new Configuracion(clave, String.valueOf(valor), this.tipo));
	}

	public String recuperar(String clave) {
		return this.config.get(clave).getValor();
	}

	public TipoConfiguracion getConfigType() {
		return this.tipo;
	}

	public Set<Configuracion> getConfigurations() {
		return new HashSet<>(this.config.values());
	}

	public boolean estaVacia() {
		return this.config.isEmpty();
	}

}
