package config;

import enumerados.TipoConfiguracion;

public class TableConfiguration {

	private Configuration config;

	public TableConfiguration() {
		this.config = new Configuration(TipoConfiguracion.TABLA);
	}
	
	public TableConfiguration(Configuration config) {
		this.config = config;
	}

	public void setAnchoColumnaId(Double valor) {
		this.config.agregar("anchoColumnaId", valor);
	}

	public void setAnchoColumnaNombre(Double valor) {
		this.config.agregar("anchoColumnaNombre", valor);
	}

	public void setAnchoColumnaAnio(Double valor) {
		this.config.agregar("anchoColumnaAnio", valor);
	}

	public void setAnchoColumnaPeriodo(Double valor) {
		this.config.agregar("anchoColumnaPeriodo", valor);
	}

	public void setAnchoColumnaNota(Double valor) {
		this.config.agregar("anchoColumnaNota", valor);
	}

	public void setAnchoColumnaEstado(Double valor) {
		this.config.agregar("anchoColumnaEstado", valor);
	}

	public void setAnchoColumnaTipo(Double valor) {
		this.config.agregar("anchoColumnaTipo", valor);
	}

	public void setAnchoColumnaHS(Double valor) {
		this.config.agregar("anchoColumnaHS", valor);
	}

	public void setAnchoColumnaCreditos(Double valor) {
		this.config.agregar("anchoColumnaCreditos", valor);
	}

	public void setIdVisible(Boolean visible) {
		this.config.agregar("idVisible", visible);
	}

	public void setNombreVisible(Boolean visible) {
		this.config.agregar("nombreVisible", visible);
	}

	public void setAnioVisible(Boolean visible) {
		this.config.agregar("anioVisible", visible);
	}

	public void setPeriodoVisible(Boolean visible) {
		this.config.agregar("periodoVisible", visible);
	}

	public void setNotaVisible(Boolean visible) {
		this.config.agregar("notaVisible", visible);
	}

	public void setEstadoVisible(Boolean visible) {
		this.config.agregar("estadoVisible", visible);
	}

	public void setTipoVisible(Boolean visible) {
		this.config.agregar("tipoVisible", visible);
	}

	public void setHSVisible(Boolean visible) {
		this.config.agregar("hsVisible", visible);
	}

	public void setCreditosVisible(Boolean visible) {
		this.config.agregar("creditosVisible", visible);
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

	private Double recuperarDouble(String clave) {
		return Double.valueOf(this.config.recuperar(clave));
	}

	public Boolean getIdVisible() {
		return this.recuperarBool("idVisible");
	}

	public Boolean getNombreVisible() {
		return this.recuperarBool("nombreVisible");
	}

	public Boolean getAnioVisible() {
		return this.recuperarBool("anioVisible");
	}

	public Boolean getPeriodoVisible() {
		return this.recuperarBool("periodoVisible");
	}

	public Boolean getNotaVisible() {
		return this.recuperarBool("notaVisible");
	}

	public Boolean getEstadoVisible() {
		return this.recuperarBool("estadoVisible");
	}

	public Boolean getTipoVisible() {
		return this.recuperarBool("tipoVisible");
	}

	public Boolean getHSVisible() {
		return this.recuperarBool("hsVisible");
	}

	public Boolean getCreditosVisible() {
		return this.recuperarBool("creditosVisible");
	}

	private Boolean recuperarBool(String clave) {
		return Boolean.valueOf(this.config.recuperar(clave));
	}

	public boolean esValida() {
		return !config.estaVacia();
	}

	public Configuration getConfig() {
		return this.config;
	}

}
