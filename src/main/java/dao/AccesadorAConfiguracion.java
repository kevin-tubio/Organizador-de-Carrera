package dao;

import java.util.Set;

import config.Configuration;
import entity.ConfigurationItem;

public class AccesadorAConfiguracion<T extends Configuration> extends AccesadorADatos<ConfigurationItem> {

	public AccesadorAConfiguracion() {
		super(ConfigurationItem.class);
	}

	public void persistirConfiguracion(T configurable) {
		persistir(configurable.getConfigurations());
	}

	public void actualizarConfiguracion(T configurable) {
		actualizar(configurable.getConfigurations());
	}

	private void persistir(Set<ConfigurationItem> configuraciones) {
		super.ejecutarTransaccion(entityManager -> configuraciones.forEach(entityManager::persist));
	}

	private void actualizar(Set<ConfigurationItem> configuraciones) {
		super.ejecutarTransaccion(entityManager -> configuraciones.forEach(entityManager::merge));
	}

	public T obtenerConfiguracion(T configurable) {
		super.obtenerTodos("WHERE TIPO = '" + configurable.getConfigType().toString() + "'").forEach(config -> agregarConfig(configurable, config));
		return configurable;
	}

	public void agregarConfig(T configurable, ConfigurationItem configuracion) {
		configurable.agregar(configuracion.getParametro(), configuracion.getValor());
	}
}
