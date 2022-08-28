package com.organizadorcarrera.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import io.reactivex.disposables.CompositeDisposable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import com.organizadorcarrera.config.TableConfiguration;
import com.organizadorcarrera.entity.Course;
import com.organizadorcarrera.enumerados.CourseStatus;
import com.organizadorcarrera.enumerados.CoursePeriod;
import com.organizadorcarrera.enumerados.CourseType;
import com.organizadorcarrera.program.Program;
import com.organizadorcarrera.enumerados.ConfigurationType;
import com.organizadorcarrera.service.ConfigurationService;
import com.organizadorcarrera.util.SpinnerTableCell;

import io.reactivex.rxjavafx.observables.JavaFxObservable;

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
	private MenuItem addCourseMenuItem;

	@FXML
	private MenuItem editCourseMenuItem;

	@FXML
	private MenuItem deleteCourseMenuItem;

	private final ConfigurationService configurationService;
	private final CompositeDisposable subscriptions;

	private MainController mainController;

	@Autowired
	public TableController(ConfigurationService configurationService) {
		this.configurationService = configurationService;
		this.subscriptions = new CompositeDisposable();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.initializeTable(resourceBundle);
		this.subscribeToProgram();
		this.editCourseMenuItem.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
		this.deleteCourseMenuItem.disableProperty().bind(tableView.getSelectionModel().selectedItemProperty().isNull());
		subscribeToEvents();
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
		initializeEditableTableCells();
	}

	private void initializeEditableTableCells() {
		tableView.setEditable(true);
		nameColumn.setCellFactory(TextFieldTableCell.<Course>forTableColumn());
		yearColumn.setCellFactory(SpinnerTableCell.<Course, Integer>forTableColumn(listInRange(1, 10)));
		coursePeriodColumn.setCellFactory(ChoiceBoxTableCell.<Course, CoursePeriod>forTableColumn(CoursePeriod.values()));
		courseTypeColumn.setCellFactory(ChoiceBoxTableCell.<Course, CourseType>forTableColumn(CourseType.values()));
		courseStatusColumn.setCellFactory(ChoiceBoxTableCell.<Course, CourseStatus>forTableColumn(CourseStatus.values()));
		gradeColumn.setCellFactory(SpinnerTableCell.<Course, String>forTableColumn("4", "5", "6", "7", "8", "9", "10"));
		creditsColumn.setCellFactory(SpinnerTableCell.<Course, Double>forTableColumn(listInRange(BigDecimal.valueOf(0), BigDecimal.valueOf(20))));
		hoursColumn.setCellFactory(SpinnerTableCell.<Course, Integer>forTableColumn(listInRange(0, 20)));
		registerTableCellChangeListeners();
	}

	private ObservableList<Integer> listInRange(int from, int to) {
		return FXCollections.observableList(IntStream.rangeClosed(from, to).boxed().collect(Collectors.toList()));
	}

	private ObservableList<Double> listInRange(BigDecimal from, BigDecimal to) {
		List<Double> list = Stream.iterate(from, d -> d.compareTo(to) <= 0, d -> d.add(BigDecimal.valueOf(1)))
				.mapToDouble(BigDecimal::doubleValue).boxed().collect(Collectors.toList());
		return FXCollections.observableList(list);
	}

	private void registerTableCellChangeListeners() {
		yearColumn.setOnEditCommit(handleYearCellEdit());
		courseStatusColumn.setOnEditCommit(handleCourseStatusCellEdit());
		coursePeriodColumn.setOnEditCommit(handleCoursePeriodCellEdit());
		courseTypeColumn.setOnEditCommit(handleCourseTypeCellEdit());
		gradeColumn.setOnEditCommit(handleGradeCellEdit());
	}

	private EventHandler<CellEditEvent<Course, Integer>> handleYearCellEdit() {
		return handleTableCellEdit(event -> event.getRowValue().setYear(event.getNewValue()));
	}

	private EventHandler<CellEditEvent<Course, CourseStatus>> handleCourseStatusCellEdit() {
		return handleTableCellEdit(event -> event.getRowValue().setCourseStatus(event.getNewValue()));
	}

	private EventHandler<CellEditEvent<Course, CoursePeriod>> handleCoursePeriodCellEdit() {
		return handleTableCellEdit(event -> event.getRowValue().setCoursePeriod(event.getNewValue()));
	}

	private EventHandler<CellEditEvent<Course, CourseType>> handleCourseTypeCellEdit() {
		return handleTableCellEdit(event -> event.getRowValue().setCourseType(event.getNewValue()));
	}

	private EventHandler<CellEditEvent<Course, String>> handleGradeCellEdit() {
		return handleTableCellEdit(event -> {
			var course = event.getRowValue();
			course.setGrade(Integer.valueOf(event.getNewValue()));
			course.setCourseStatus(CourseStatus.APROBADA);
		});
	}

	private <T> EventHandler<CellEditEvent<Course, T>> handleTableCellEdit(Consumer<CellEditEvent<Course, T>> consumer) {
		return event -> {
			consumer.accept(event);
			tableView.refresh();
			this.mainController.emitUnsavedChanges();
		};
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

	private void subscribeToEvents() {
		subscriptions.addAll(
				JavaFxObservable.actionEventsOf(this.addCourseMenuItem).subscribe(onClick -> this.addCourse()),
				JavaFxObservable.actionEventsOf(this.editCourseMenuItem).subscribe(onClick -> this.editCourse()),
				JavaFxObservable.actionEventsOf(this.deleteCourseMenuItem).subscribe(onClick -> this.deleteCourse())
		);
	}

	public void enableActions() {
		if (this.getSelectedItem() != null) {
			this.mainController.enableActions();
		}
	}

	protected Course getSelectedItem() {
		return this.tableView.getSelectionModel().getSelectedItem();
	}

	private void deleteCourse() {
		this.mainController.deletCourse(this.getSelectedItem());
	}

	private void editCourse() {
		this.mainController.editCourse(this.getSelectedItem());
		tableView.refresh();
	}

	private void addCourse() {
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
