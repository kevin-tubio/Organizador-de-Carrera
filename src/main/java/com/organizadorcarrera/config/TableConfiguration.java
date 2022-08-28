package com.organizadorcarrera.config;

import com.organizadorcarrera.model.Configuration;
import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.enums.ConfigurationType;

import javafx.scene.control.TableColumn;

public class TableConfiguration {

	private final Configuration configuration;

	public TableConfiguration() {
		this.configuration = new Configuration(ConfigurationType.TABLE);
	}

	public TableConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setColumnOrder(TableColumn<Course, ?> column, Integer order) {
		this.configuration.addConfigurationPair(getConfigurationId(column, "Order"), order);
	}

	public Integer getColumnOrder(TableColumn<Course, ?> column) {
		return getInteger(column);
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

	private Integer getInteger(TableColumn<Course, ?> column) {
		return Integer.valueOf(this.configuration.getConfigurationValue(getConfigurationId(column, "Order")));
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
