package com.organizadorcarrera.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
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

import com.organizadorcarrera.converter.TableViewConfigurationConverter;
import com.organizadorcarrera.service.TableConfigurationService;
import com.organizadorcarrera.model.Configuration;
import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.enums.CourseStatus;
import com.organizadorcarrera.enums.CoursePeriod;
import com.organizadorcarrera.enums.CourseType;
import com.organizadorcarrera.util.SpinnerTableCell;

import io.reactivex.disposables.CompositeDisposable;
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

	private final TableConfigurationService tableConfigurationService;
	private final CompositeDisposable subscriptions;
	private final MainController mainController;
	private final ObservableList<Course> courseList;
	private final TableViewConfigurationConverter tableViewConfigurationConverter;

	@Autowired
	public TableController(
			MainController mainController,
			TableConfigurationService tableConfigurationService,
			CompositeDisposable compositeDisposable,
			ObservableList<Course> courseList,
			TableViewConfigurationConverter tableViewConfigurationConverter) {

		this.mainController = mainController;
		this.tableConfigurationService = tableConfigurationService;
		this.subscriptions = compositeDisposable;
		this.courseList = courseList;
		this.tableViewConfigurationConverter = tableViewConfigurationConverter;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.initializeTable(resourceBundle);
		this.tableView.setItems(courseList);
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
		coursePeriodColumn.setComparator(Comparator.comparing(CoursePeriod::toString));
		courseStatusColumn.setComparator(Comparator.comparing(CourseStatus::toString));
		courseTypeColumn.setComparator(Comparator.comparing(CourseType::toString));
		gradeColumn.setComparator((o1, o2) -> {
			if (o1.matches("\\d+") && o2.matches("\\d+"))
				return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
			return o1.compareTo(o2);
		});
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
		Configuration configuration = tableConfigurationService.getTableConfiguration();
		tableViewConfigurationConverter.fromTable(tableView, configuration);
		tableConfigurationService.saveTableConfiguration(configuration);
	}

	public void loadTableConfiguration() {
		Configuration configuration = tableConfigurationService.getTableConfiguration();
		tableViewConfigurationConverter.fromConfiguration(tableView, configuration);
	}

}
