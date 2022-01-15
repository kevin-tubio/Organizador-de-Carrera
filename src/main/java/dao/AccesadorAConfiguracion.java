package dao;

import java.util.Set;

import dto.Configurable;
import entity.Configuracion;

public class AccesadorAConfiguracion<T extends Configurable> extends AccesadorADatos<Configuracion> {

	public AccesadorAConfiguracion() {
		super(Configuracion.class);
	}

	public void persistirConfiguracion(T configurable) {
		persistir(configurable.getConfigurations());
	}

	public void actualizarConfiguracion(T configurable) {
		actualizar(configurable.getConfigurations());
	}

	private void persistir(Set<Configuracion> configuraciones) {
		super.ejecutarTransaccion(entityManager -> configuraciones.forEach(entityManager::persist));
	}

	private void actualizar(Set<Configuracion> configuraciones) {
		super.ejecutarTransaccion(entityManager -> configuraciones.forEach(entityManager::merge));
	}

	public T obtenerConfiguracion(T configurable) {
		super.obtenerTodos("WHERE TIPO = '" + configurable.getConfigType().toString() + "'").forEach(config -> agregarConfig(configurable, config));
		return configurable;
	}

	public void agregarConfig(T configurable, Configuracion configuracion) {
		configurable.agregar(configuracion.getParametro(), configuracion.getValor());
	}
}
