package com.organizadorcarrera.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import com.organizadorcarrera.config.TableConfiguration;
import com.organizadorcarrera.entity.Course;
import com.organizadorcarrera.enumerados.CourseStatus;
import com.organizadorcarrera.enumerados.CoursePeriod;
import com.organizadorcarrera.enumerados.CourseType;
import com.organizadorcarrera.program.Program;
import com.organizadorcarrera.enumerados.ConfigurationType;
import com.organizadorcarrera.service.ConfigurationService;

@Component
public class TableController implements Initializable {

	@FXML
	private TableView<Course> tableView;

	@FXML
	private TableColumn<Course, Integer> idColumn;

	@FXML
	private TableColumn<Course, String> nameColumn;

	@FXML
	private TableColumn<Course, Integer> yearColumn;

	@FXML
	private TableColumn<Course, CoursePeriod> coursePeriodColumn;

	@FXML
	private TableColumn<Course, String> gradeColumn;

	@FXML
	private TableColumn<Course, CourseStatus> courseStatusColumn;

	@FXML
	private TableColumn<Course, CourseType> courseTypeColumn;

	@FXML
	private TableColumn<Course, Integer> hoursColumn;

	@FXML
	private TableColumn<Course, Double> creditsColumn;

	@FXML
	private MenuItem editCourseMenuItem;

	@FXML
	private MenuItem deleteCourseMenuItem;

	@Autowired
	private ConfigurationService configurationService;

	private MainController mainController;

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.initializeTable(resourceBundle);
		this.subscribeToProgram();
		this.editCourseMenuItem.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
		this.deleteCourseMenuItem.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
	}

	private void initializeTable(ResourceBundle resourceBundle) {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
		coursePeriodColumn.setCellValueFactory(new PropertyValueFactory<>("coursePeriod"));
		gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
		courseStatusColumn.setCellValueFactory(new PropertyValueFactory<>("courseStatus"));
		courseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("courseType"));
		hoursColumn.setCellValueFactory(new PropertyValueFactory<>("hours"));
		creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
		setColumnComparators();
		tableView.setPlaceholder(new Label(resourceBundle.getString("ListadoVacio")));
		inicializeEditableTableCells();
	}

	private void inicializeEditableTableCells() {
		tableView.setEditable(true);
		courseStatusColumn.setCellFactory(ChoiceBoxTableCell.<Course, CourseStatus>forTableColumn(CourseStatus.values()));
		registerEventListeners();
	}

	private void registerEventListeners() {
		courseStatusColumn.setOnEditCommit(event -> {
			event.getRowValue().setCourseStatus(event.getNewValue());
			tableView.refresh();
			this.mainController.emitUnsavedChanges();
		});
	}

	private void setColumnComparators() {
		coursePeriodColumn.setComparator((o1, o2) -> o1.toString().compareTo(o2.toString()));
		courseStatusColumn.setComparator((o1, o2) -> o1.toString().compareTo(o2.toString()));
		courseTypeColumn.setComparator((o1, o2) -> o1.toString().compareTo(o2.toString()));
		gradeColumn.setComparator((o1, o2) -> {
			if (o1.matches("\\d+") && o2.matches("\\d+"))
				return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
			return o1.compareTo(o2);
		});
	}

	private void subscribeToProgram() {
		MapChangeListener<Integer, Course> listener = change -> tableView.getItems().setAll(change.getMap().values());
		Program.getInstance().getProgramMap().addListener(listener);
	}

	public void enableActions() {
		if (this.getSelectedItem() != null) {
			this.mainController.enableActions();
		}
	}

	protected Course getSelectedItem() {
		return this.tableView.getSelectionModel().getSelectedItem();
	}

	public void deleteCourse() {
		this.mainController.deletCourse(this.getSelectedItem());
	}

	public void editCourse() {
		this.mainController.editCourse(this.getSelectedItem());
		tableView.refresh();
	}

	public void addCourse() {
		this.mainController.addCourse();
	}

	public void saveTableConfiguration() {
		TableConfiguration configuration = new TableConfiguration();
		saveColumnsOrder(configuration);
		saveColumnsWidth(configuration);
		saveVisibleColumns(configuration);
		configurationService.save(configuration.getConfiguration());
	}

	private void saveColumnsOrder(TableConfiguration configuration) {
		this.tableView.getColumns().forEach(column -> configuration.setColumnOrder(column, getColumnIndex(column)));
	}

	private int getColumnIndex(TableColumn<Course, ?> column) {
		return this.tableView.getColumns().indexOf(column);
	}

	private void saveColumnsWidth(TableConfiguration configuration) {
		this.tableView.getColumns().forEach(configuration::setColumnWidth);
	}

	private void saveVisibleColumns(TableConfiguration configuration) {
		this.tableView.getColumns().forEach(configuration::setColumnVisible);
	}

	public void loadTableConfiguration() {
		TableConfiguration configuration = new TableConfiguration(configurationService.findByTipoConfiguracion(ConfigurationType.TABLE));
		if (configuration.isValid()) {
			loadColumnsOrder(configuration);
			loadColumnsWidth(configuration);
			loadVisibleColumns(configuration);
		}
	}

	private void loadColumnsOrder(TableConfiguration configuration) {
		this.tableView.getColumns().sort((column, otherColumn) -> Integer.compare(configuration.getColumnOrder(column),
				configuration.getColumnOrder(otherColumn)));
	}

	private void loadColumnsWidth(TableConfiguration configuration) {
		this.tableView.getColumns().forEach(configuration::getColumnWidth);
	}

	private void loadVisibleColumns(TableConfiguration configuration) {
		this.tableView.getColumns().forEach(configuration::getColumnVisible);
	}

	@Autowired
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

}
