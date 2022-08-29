package com.organizadorcarrera.util.converter;

import com.organizadorcarrera.model.Configuration;
import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.service.TableConfigurationService;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class TableViewConfigurationConverter {

    private final TableConfigurationService tableConfigurationService;

    @Autowired
    public TableViewConfigurationConverter(TableConfigurationService tableConfigurationService) {
        this.tableConfigurationService = tableConfigurationService;
    }

    public void fromTable(TableView<Course> table, Configuration configuration) {
        saveColumnsOrder(table, configuration);
        saveColumnsWidth(table, configuration);
        saveVisibleColumns(table, configuration);
    }

    private void saveColumnsOrder(TableView<Course> table, Configuration configuration) {
        table.getColumns().forEach(column -> setColumnOrder(column, getColumnIndex(table, column), configuration));
    }

    private void setColumnOrder(TableColumn<Course, ?> column, Integer order, Configuration configuration) {
        configuration.addConfigurationPair(getConfigurationId(column, "Order"), order);
    }

    private String getConfigurationId(TableColumn<Course, ?> column, String type) {
        return String.format("%s%s", column.getId(), type);
    }

    private int getColumnIndex(TableView<Course> table, TableColumn<Course, ?> column) {
        return table.getColumns().indexOf(column);
    }

    private void saveColumnsWidth(TableView<Course> table, Configuration configuration) {
        table.getColumns().forEach(column -> setColumnWidth(column, configuration));
    }

    private void setColumnWidth(TableColumn<Course, ?> column, Configuration configuration) {
        configuration.addConfigurationPair(getConfigurationId(column, "Width"), column.getWidth());
    }

    private void saveVisibleColumns(TableView<Course> table, Configuration configuration) {
        table.getColumns().forEach(column -> setColumnVisible(column, configuration));
    }

    private void setColumnVisible(TableColumn<Course, ?> column, Configuration configuration) {
        configuration.addConfigurationPair(getConfigurationId(column, "Visible"), column.isVisible());
    }

    public void fromConfiguration(TableView<Course> table, Configuration configuration) {
        if (!tableConfigurationService.isConfigurationValid(configuration)) {
            return;
        }

        loadColumnsOrder(table, configuration);
        loadColumnsWidth(table, configuration);
        loadVisibleColumns(table, configuration);
    }

    private void loadColumnsOrder(TableView<Course> table, Configuration configuration) {
        table.getColumns().sort(Comparator.comparingInt(column -> getColumnOrder(column, configuration)));
    }

    private Integer getColumnOrder(TableColumn<Course, ?> column, Configuration configuration) {
        return getInteger(column, configuration);
    }

    private Integer getInteger(TableColumn<Course, ?> column, Configuration configuration) {
        return Integer.valueOf(configuration.getConfigurationValue(getConfigurationId(column, "Order")));
    }

    private void loadColumnsWidth(TableView<Course> table, Configuration configuration) {
        table.getColumns().forEach(column -> getColumnWidth(column, configuration));
    }

    private void getColumnWidth(TableColumn<Course, ?> column, Configuration configuration) {
        column.setPrefWidth(getDouble(column, configuration));
    }

    private Double getDouble(TableColumn<Course, ?> column, Configuration configuration) {
        return Double.valueOf(configuration.getConfigurationValue(getConfigurationId(column, "Width")));
    }

    private void loadVisibleColumns(TableView<Course> table, Configuration configuration) {
        table.getColumns().forEach(column -> getColumnVisible(column, configuration));
    }

    private void getColumnVisible(TableColumn<Course, ?> column, Configuration configuration) {
        column.setVisible(getBoolean(column, configuration));
    }

    private Boolean getBoolean(TableColumn<Course, ?> column, Configuration configuration) {
        return Boolean.valueOf(configuration.getConfigurationValue(getConfigurationId(column, "Visible")));
    }

}
