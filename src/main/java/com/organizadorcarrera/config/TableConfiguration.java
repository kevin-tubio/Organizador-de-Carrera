package com.organizadorcarrera.config;

import com.organizadorcarrera.entity.Course;
import com.organizadorcarrera.enumerados.ConfigurationType;

import javafx.scene.control.TableColumn;

public class TableConfiguration {

	private Configuration configuration;

	public TableConfiguration() {
		this.configuration = new Configuration(ConfigurationType.TABLE);
	}

	public TableConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setColumnWidth(TableColumn<Course, ?> column) {
		this.configuration.addConfigurationPair(getConfigurationId(column, "Width"), column.getWidth());
	}

	public void getColumnWidth(TableColumn<Course, ?> column) {
		column.setPrefWidth(getDouble(column));
	}

	public void setColumnVisible(TableColumn<Course, ?> column) {
		this.configuration.addConfigurationPair(getConfigurationId(column, "Visible"), column.isVisible());
	}

	public void getColumnVisible(TableColumn<Course, ?> column) {
		column.setVisible(getBoolean(column));
	}

	private String getConfigurationId(TableColumn<Course, ?> column, String type) {
		return String.format("%s%s", column.getId(), type);
	}

	private Double getDouble(TableColumn<Course, ?> column) {
		return Double.valueOf(this.configuration.getConfigurationValue(getConfigurationId(column, "Width")));
	}

	private Boolean getBoolean(TableColumn<Course, ?> column) {
		return Boolean.valueOf(this.configuration.getConfigurationValue(getConfigurationId(column, "Visible")));
	}

	public boolean isValid() {
		return configuration != null && configuration.isValid();
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

}
