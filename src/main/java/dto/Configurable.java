package dto;

import java.util.Set;

import entity.Configuracion;
import enumerados.TipoConfiguracion;

public interface Configurable {

	public <T> void agregar(String clave, T valor);

	public TipoConfiguracion getConfigType();

	public Set<Configuracion> getConfigurations();

}
