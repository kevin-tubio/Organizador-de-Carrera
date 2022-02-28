package com.organizadorcarrera.config;

import com.organizadorcarrera.enumerados.ConfigurationType;

public class TableConfiguration {

	private Configuration configuration;

	public TableConfiguration() {
		this.configuration = new Configuration(ConfigurationType.TABLE);
	}

	public TableConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setIdColumnWidth(Double width) {
		this.configuration.addConfigurationPair("anchoColumnaId", width);
	}

	public void setNameColumnWidth(Double width) {
		this.configuration.addConfigurationPair("anchoColumnaNombre", width);
	}

	public void setAnchoColumnaAnio(Double width) {
		this.configuration.addConfigurationPair("anchoColumnaAnio", width);
	}

	public void setCoursePeriodColumnWidth(Double width) {
		this.configuration.addConfigurationPair("anchoColumnaPeriodo", width);
	}

	public void setAnchoColumnaNota(Double width) {
		this.configuration.addConfigurationPair("anchoColumnaNota", width);
	}

	public void setCourseStatusColumnWidth(Double width) {
		this.configuration.addConfigurationPair("anchoColumnaEstado", width);
	}

	public void setCourseTypeColumnWidth(Double width) {
		this.configuration.addConfigurationPair("anchoColumnaTipo", width);
	}

	public void setAnchoColumnaHS(Double width) {
		this.configuration.addConfigurationPair("anchoColumnaHS", width);
	}

	public void setAnchoColumnaCreditos(Double width) {
		this.configuration.addConfigurationPair("anchoColumnaCreditos", width);
	}

	public void setIdVisible(Boolean visible) {
		this.configuration.addConfigurationPair("idVisible", visible);
	}

	public void setCourseNameVisible(Boolean visible) {
		this.configuration.addConfigurationPair("nombreVisible", visible);
	}

	public void setAnioVisible(Boolean visible) {
		this.configuration.addConfigurationPair("anioVisible", visible);
	}

	public void setCoursePeriodVisible(Boolean visible) {
		this.configuration.addConfigurationPair("periodoVisible", visible);
	}

	public void setNotaVisible(Boolean visible) {
		this.configuration.addConfigurationPair("notaVisible", visible);
	}

	public void setCourseStatusVisible(Boolean visible) {
		this.configuration.addConfigurationPair("estadoVisible", visible);
	}

	public void setCourseTypeVisible(Boolean visible) {
		this.configuration.addConfigurationPair("tipoVisible", visible);
	}

	public void setHSVisible(Boolean visible) {
		this.configuration.addConfigurationPair("hsVisible", visible);
	}

	public void setCreditosVisible(Boolean visible) {
		this.configuration.addConfigurationPair("creditosVisible", visible);
	}

	public Double getIdColumnWidth() {
		return this.getDouble("anchoColumnaId");
	}

	public Double getNameColumnWidth() {
		return this.getDouble("anchoColumnaNombre");
	}

	public Double getYearColumnWidth() {
		return this.getDouble("anchoColumnaAnio");
	}

	public Double getCoursePeriodColumnWidth() {
		return this.getDouble("anchoColumnaPeriodo");
	}

	public Double getGradeColumnWidth() {
		return this.getDouble("anchoColumnaNota");
	}

	public Double getCourseStatusColumnWidth() {
		return this.getDouble("anchoColumnaEstado");
	}

	public Double getCourseTypeColumnWidth() {
		return this.getDouble("anchoColumnaTipo");
	}

	public Double getAnchoColumnaHS() {
		return this.getDouble("anchoColumnaHS");
	}

	public Double getAnchoColumnaCreditos() {
		return this.getDouble("anchoColumnaCreditos");
	}

	private Double getDouble(String clave) {
		return Double.valueOf(this.configuration.getConfigurationValue(clave));
	}

	public Boolean getIdVisible() {
		return this.getBoolean("idVisible");
	}

	public Boolean getNameVisible() {
		return this.getBoolean("nombreVisible");
	}

	public Boolean getYearVisible() {
		return this.getBoolean("anioVisible");
	}

	public Boolean getCoursePeriodVisible() {
		return this.getBoolean("periodoVisible");
	}

	public Boolean getGradeVisible() {
		return this.getBoolean("notaVisible");
	}

	public Boolean getCourseStatusVisible() {
		return this.getBoolean("estadoVisible");
	}

	public Boolean getCourseTypeVisible() {
		return this.getBoolean("tipoVisible");
	}

	public Boolean getHSVisible() {
		return this.getBoolean("hsVisible");
	}

	public Boolean getCreditosVisible() {
		return this.getBoolean("creditosVisible");
	}

	private Boolean getBoolean(String key) {
		return Boolean.valueOf(this.configuration.getConfigurationValue(key));
	}

	public boolean isValid() {
		return configuration != null && configuration.isValid();
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

}
