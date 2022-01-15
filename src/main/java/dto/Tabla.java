package dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import entity.Configuracion;
import enumerados.TipoConfiguracion;

public class Tabla implements Configurable {

	private Map<String, Configuracion> config;

	private TipoConfiguracion tipo;
	
	public Tabla() {
		this.config = new HashMap<>();
		this.tipo = TipoConfiguracion.TABLA;
	}

	public void setAnchoColumnaId(Double valor) {
		this.agregar("anchoColumnaId", valor);
	}

	public void setAnchoColumnaNombre(Double valor) {
		this.agregar("anchoColumnaNombre", valor);
	}

	public void setAnchoColumnaAnio(Double valor) {
		this.agregar("anchoColumnaAnio", valor);
	}

	public void setAnchoColumnaPeriodo(Double valor) {
		this.agregar("anchoColumnaPeriodo", valor);
	}

	public void setAnchoColumnaNota(Double valor) {
		this.agregar("anchoColumnaNota", valor);
	}

	public void setAnchoColumnaEstado(Double valor) {
		this.agregar("anchoColumnaEstado", valor);
	}

	public void setAnchoColumnaTipo(Double valor) {
		this.agregar("anchoColumnaTipo", valor);
	}

	public void setAnchoColumnaHS(Double valor) {
		this.agregar("anchoColumnaHS", valor);
	}

	public void setAnchoColumnaCreditos(Double valor) {
		this.agregar("anchoColumnaCreditos", valor);
	}

	public Double getAnchoColumnaId() {
		return this.recuperarDouble("anchoColumnaId");
	}

	public Double getAnchoColumnaNombre() {
		return this.recuperarDouble("anchoColumnaNombre");
	}

	public Double getAnchoColumnaAnio() {
		return this.recuperarDouble("anchoColumnaAnio");
	}

	public Double getAnchoColumnaPeriodo() {
		return this.recuperarDouble("anchoColumnaPeriodo");
	}

	public Double getAnchoColumnaNota() {
		return this.recuperarDouble("anchoColumnaNota");
	}

	public Double getAnchoColumnaEstado() {
		return this.recuperarDouble("anchoColumnaEstado");
	}

	public Double getAnchoColumnaTipo() {
		return this.recuperarDouble("anchoColumnaTipo");
	}

	public Double getAnchoColumnaHS() {
		return this.recuperarDouble("anchoColumnaHS");
	}

	public Double getAnchoColumnaCreditos() {
		return this.recuperarDouble("anchoColumnaCreditos");
	}

	@Override
	public <T> void agregar(String clave, T valor) {
		this.config.put(clave, new Configuracion(clave, String.valueOf(valor), this.tipo));
	}

	private Double recuperarDouble(String clave) {
		return Double.valueOf(this.recuperar(clave));
	}
	
	private String recuperar(String clave) {
		return this.config.get(clave).getValor();
	}

	@Override
	public TipoConfiguracion getConfigType() {
		return this.tipo;
	}

	public boolean esValida() {
		return !this.config.isEmpty();
	}

	@Override
	public Set<Configuracion> getConfigurations() {
		return new HashSet<>(this.config.values());
	}

}
