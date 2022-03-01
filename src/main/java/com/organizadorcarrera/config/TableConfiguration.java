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
		this.configuration.addConfigurationPair("idColumnWidth", width);
	}

	public void setNameColumnWidth(Double width) {
		this.configuration.addConfigurationPair("nameColumnWidth", width);
	}

	public void setYearColumnWidth(Double width) {
		this.configuration.addConfigurationPair("yearColumnWidth", width);
	}

	public void setCoursePeriodColumnWidth(Double width) {
		this.configuration.addConfigurationPair("coursePeriodColumnWidth", width);
	}

	public void setGradeColumnWidth(Double width) {
		this.configuration.addConfigurationPair("gradeColumnWidth", width);
	}

	public void setCourseStatusColumnWidth(Double width) {
		this.configuration.addConfigurationPair("courseStatusColumnWidth", width);
	}

	public void setCourseTypeColumnWidth(Double width) {
		this.configuration.addConfigurationPair("courseTypeColumnWidth", width);
	}

	public void setHoursColumnWidth(Double width) {
		this.configuration.addConfigurationPair("hoursColumnWidth", width);
	}

	public void setCreditsColumnWidth(Double width) {
		this.configuration.addConfigurationPair("creditsColumnWidth", width);
	}

	public void setIdColumnVisible(Boolean visible) {
		this.configuration.addConfigurationPair("idColumnVisible", visible);
	}

	public void setNameColumnVisible(Boolean visible) {
		this.configuration.addConfigurationPair("nameColumnVisible", visible);
	}

	public void setYearColumnVisible(Boolean visible) {
		this.configuration.addConfigurationPair("yearColumnVisible", visible);
	}

	public void setCoursePeriodColumnVisible(Boolean visible) {
		this.configuration.addConfigurationPair("coursePeriodColumnVisible", visible);
	}

	public void setGradeColumnVisible(Boolean visible) {
		this.configuration.addConfigurationPair("gradeColumnVisible", visible);
	}

	public void setCourseStatusColumnVisible(Boolean visible) {
		this.configuration.addConfigurationPair("courseStatusColumnVisible", visible);
	}

	public void setCourseTypeColumnVisible(Boolean visible) {
		this.configuration.addConfigurationPair("courseTypeColumnVisible", visible);
	}

	public void setHoursColumnVisible(Boolean visible) {
		this.configuration.addConfigurationPair("hoursColumnVisible", visible);
	}

	public void setCreditsColumnVisible(Boolean visible) {
		this.configuration.addConfigurationPair("creditsColumnVisible", visible);
	}

	public Double getIdColumnWidth() {
		return this.getDouble("idColumnWidth");
	}

	public Double getNameColumnWidth() {
		return this.getDouble("nameColumnWidth");
	}

	public Double getYearColumnWidth() {
		return this.getDouble("yearColumnWidth");
	}

	public Double getCoursePeriodColumnWidth() {
		return this.getDouble("coursePeriodColumnWidth");
	}

	public Double getGradeColumnWidth() {
		return this.getDouble("gradeColumnWidth");
	}

	public Double getCourseStatusColumnWidth() {
		return this.getDouble("courseStatusColumnWidth");
	}

	public Double getCourseTypeColumnWidth() {
		return this.getDouble("courseTypeColumnWidth");
	}

	public Double getHoursColumnWidth() {
		return this.getDouble("hoursColumnWidth");
	}

	public Double getCreditsColumnWidth() {
		return this.getDouble("creditsColumnWidth");
	}

	private Double getDouble(String clave) {
		return Double.valueOf(this.configuration.getConfigurationValue(clave));
	}

	public Boolean getIdColumnVisible() {
		return this.getBoolean("idColumnVisible");
	}

	public Boolean getNameColumnVisible() {
		return this.getBoolean("nameColumnVisible");
	}

	public Boolean getYearColumnVisible() {
		return this.getBoolean("yearColumnVisible");
	}

	public Boolean getCoursePeriodColumnVisible() {
		return this.getBoolean("coursePeriodColumnVisible");
	}

	public Boolean getGradeColumnVisible() {
		return this.getBoolean("gradeColumnVisible");
	}

	public Boolean getCourseStatusColumnVisible() {
		return this.getBoolean("courseStatusColumnVisible");
	}

	public Boolean getCourseTypeColumnVisible() {
		return this.getBoolean("courseTypeColumnVisible");
	}

	public Boolean getHoursColumnVisible() {
		return this.getBoolean("hoursColumnVisible");
	}

	public Boolean getCreditsColumnVisible() {
		return this.getBoolean("creditsColumnVisible");
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
