package com.organizadorcarrera.controller;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

import io.reactivex.disposables.CompositeDisposable;

import javafx.collections.ListChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.organizadorcarrera.enumerados.CourseStatus;
import com.organizadorcarrera.enumerados.CoursePeriod;
import com.organizadorcarrera.enumerados.CourseType;
import com.organizadorcarrera.program.Program;
import com.organizadorcarrera.util.LangResource;
import com.organizadorcarrera.entity.Course;

import io.reactivex.rxjavafx.observables.JavaFxObservable;

import net.synedra.validatorfx.Check;
import net.synedra.validatorfx.ValidationResult;
import net.synedra.validatorfx.Validator;

@Component
@Scope("prototype")
public class CourseController implements Initializable {

	@FXML
	private Button okButton;

	@FXML
	private Button cancelButton;

	@FXML
	private TextField idField;

	@FXML
	private TextField nameField;

	@FXML
	private TextField searchField;

	@FXML
	private ChoiceBox<CourseType> courseTypeField;

	@FXML
	private ChoiceBox<CoursePeriod> coursePeriodField;

	@FXML
	private ChoiceBox<CourseStatus> courseStatusField;

	@FXML
	private Spinner<Integer> gradeField;

	@FXML
	private Spinner<Integer> yearField;

	@FXML
	private Spinner<Integer> hoursField;

	@FXML
	private Spinner<Double> creditsField;

	@FXML
	private ListView<Course> courseList;

	@FXML
	private ListView<Course> correlativeCourseList;

	@FXML
	private MenuItem addCorrelativeMenuItem;

	@FXML
	private MenuItem removeCorrelativeMenuItem;

	@FXML
	private Label errorMessage;

	@FXML
	private BorderPane container;

	private final CompositeDisposable subscriptions;
	private final ObservableList<Course> correlativeCourses;
	private final FilteredList<Course> filteredList;
	private final Validator validator;
	private final Program program;
	private final MainController mainController;

	private Course injectedCourse;

	@Autowired
	public CourseController(
			MainController mainController,
			CompositeDisposable compositeDisposable,
			Program program,
			Validator validator,
			FilteredList<Course> filteredCourseList,
			ObservableList<Course> correlativeCourses) {

		this.mainController = mainController;
		this.program = program;
		this.validator = validator;
		this.correlativeCourses = correlativeCourses;
		this.filteredList = filteredCourseList;
		this.subscriptions = compositeDisposable;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.initializeFields();
		this.createFieldsValidator();
		this.subscribeToSearchBox();
		this.subscribeToEvents();
	}

	private void initializeFields() {
		this.initializeChoiceBoxes();
		this.initializeSpinners();
		this.initializeButtons();
		this.initializeCorrelativeCousesList();
		this.initializeCourseList();
	}

	private void initializeChoiceBoxes() {
		this.courseTypeField.getItems().addAll(CourseType.values());
		this.courseTypeField.setValue(CourseType.MATERIA);
		this.coursePeriodField.getItems().addAll(CoursePeriod.values());
		this.coursePeriodField.setValue(CoursePeriod.PRIMER_CUATRIMESTRE);
		this.courseStatusField.getItems().addAll(CourseStatus.values());
		this.courseStatusField.setValue(CourseStatus.NO_CURSADA);
	}

	private void initializeSpinners() {
		this.gradeField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(4, 10));
		this.gradeField.disableProperty().bind(courseStatusField.getSelectionModel().selectedItemProperty().isNotEqualTo(CourseStatus.APROBADA));
		this.yearField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
		this.yearField.getValueFactory().setValue(1);
		this.hoursField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20));
		this.hoursField.getValueFactory().setValue(4);
		this.creditsField.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 20.0));
		this.creditsField.getValueFactory().setValue(0.0);
		stylizeSpinner(this.gradeField);
		stylizeSpinner(this.yearField);
		stylizeSpinner(this.hoursField);
		stylizeSpinner(this.creditsField);
	}

	private void stylizeSpinner(Spinner<?> spinner) {
		spinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
		spinner.editorProperty().get().setAlignment(Pos.CENTER);
	}

	private void initializeButtons() {
		this.okButton.disableProperty().bind(validator.containsErrorsProperty());
		this.cancelButton.setCancelButton(true);
	}

	private void initializeCorrelativeCousesList() {
		this.correlativeCourses.addListener((ListChangeListener<? super Course>) course -> applyFilter(this.searchField.getText()));
		this.correlativeCourseList.setItems(this.correlativeCourses);
		this.correlativeCourseList.setPlaceholder(new Label(LangResource.getString("ListaCorrelativasVacia")));
		this.removeCorrelativeMenuItem.disableProperty()
				.bind(correlativeCourseList.getSelectionModel().selectedItemProperty().isNull());
	}

	private void initializeCourseList() {
		this.courseList.setVisible(false);
		this.courseList.setPlaceholder(new Label(LangResource.getString("ListadoVacio")));
		this.courseList.setItems(this.filteredList);
		this.addCorrelativeMenuItem.disableProperty().bind(courseList.getSelectionModel().selectedItemProperty().isNull());
	}

	private void createFieldsValidator() {
		var checkID = createIdCheck();
		var checkNombre = createNameCheck();
		this.idField.textProperty().addListener((observable, oldValue, newValue) -> checkID.recheck());
		this.nameField.textProperty().addListener((observable, oldValue, newValue) -> checkNombre.recheck());
		this.validator.validationResultProperty()
				.addListener((observable, oldValue, newValue) -> this.errorMessage.setText(getErrorMessage(newValue)));
	}

	private String getErrorMessage(ValidationResult result) {
		var iterador = result.getMessages().listIterator();
		return (iterador.hasNext()) ? iterador.next().getText() : "";
	}

	private Check createIdCheck() {
		return validator.createCheck().withMethod(in -> {
			if (fieldIsEmpty(this.idField))
				in.error(LangResource.getString("InputIdVacio"));
			if (!this.idField.getText().matches("^\\d+$"))
				in.error(LangResource.getString("InputIdInvalido"));
			if (idIsRepeated())
				in.error(LangResource.getString("InputIdRepetido"));
			if (courseCicles())
				in.error(LangResource.getString("InputIdCiclo"));
		}).dependsOn("id", this.idField.textProperty()).decorates(this.idField);
	}

	private boolean idIsRepeated() {
		return !idMatchesCourse(this.injectedCourse)
				&& program.containsCourse(this.idField.getText());
	}

	private boolean idMatchesCourse(Course course) {
		return course != null && this.idField.getText().equals(String.valueOf(course.getId()));
	}

	private boolean courseCicles() {
		return idMatchesCourse(this.injectedCourse) && this.correlativeCourses.contains(this.injectedCourse);
	}

	private Check createNameCheck() {
		return validator.createCheck().withMethod(in -> {
			if (fieldIsEmpty(this.nameField))
				in.error(LangResource.getString("InputNombreVacio"));
		}).dependsOn("nombre", this.nameField.textProperty()).decorates(this.nameField);
	}

	private boolean fieldIsEmpty(TextField field) {
		return field.textProperty().getValue().isBlank();
	}

	private void subscribeToSearchBox() {
		this.container.setOnMousePressed(event -> this.container.requestFocus());
		this.searchField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (Boolean.TRUE.equals(newValue)) {
				this.courseList.setVisible(true);
				applyFilter(this.searchField.getText());
			} else if (!this.courseList.isFocused()) {
				this.searchField.clear();
				this.courseList.setVisible(false);
			}
		});
		this.searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilter(newValue));
		subscribeToCourseList();
	}

	private void applyFilter(String filter) {
		this.filteredList.setPredicate(course -> !this.correlativeCourses.contains(course) && isNotFiltered(course, filter.toLowerCase()));
	}

	private boolean isNotFiltered(Course course, String filter) {
		var id = String.valueOf(course.getId());
		var nombreMateria = course.getName().toLowerCase();
		var resultado = true;
		if (!validator.containsErrors())
			resultado = !idMatchesCourse(course);
		return resultado && searchMatches(nombreMateria, id, filter);
	}

	private boolean searchMatches(String courseName, String id, String search) {
		return courseName.indexOf(search) != -1 || id.indexOf(search) != -1;
	}

	private void subscribeToCourseList() {
		this.courseList.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (Boolean.TRUE.equals(newValue))
				return;

			this.courseList.setVisible(false);
			if (!this.searchField.isFocused())
				this.searchField.clear();
		});
	}

	private void subscribeToEvents() {
		subscriptions.addAll(
				JavaFxObservable.actionEventsOf(this.cancelButton).subscribe(onClick -> this.close()),
				JavaFxObservable.actionEventsOf(this.okButton).subscribe(onClick -> this.accept()),
				JavaFxObservable.actionEventsOf(this.removeCorrelativeMenuItem).subscribe(onClick -> this.removeCorrelativeCourse()),
				JavaFxObservable.actionEventsOf(this.addCorrelativeMenuItem).subscribe(onClick -> this.addCorrelativeCourse())
		);
	}

	public void injectCourse(Course course) {
		this.injectedCourse = course;
		this.idField.setText(String.valueOf(course.getId()));
		this.nameField.setText(course.getName());
		this.coursePeriodField.setValue(course.getCoursePeriod());
		this.courseStatusField.setValue(course.getCourseStatus());
		this.yearField.getValueFactory().setValue(course.getYear());
		if (course.getCourseStatus() == CourseStatus.APROBADA)
			this.gradeField.getValueFactory().setValue(Integer.parseInt(course.getGrade()));
		this.hoursField.getValueFactory().setValue(course.getHours());
		this.courseTypeField.setValue(course.getCourseType());
		this.correlativeCourses.addAll(course.getCorrelatives());
		this.creditsField.getValueFactory().setValue(course.getCredits());
	}

	public void addCorrelativeCourse() {
		Course course = this.courseList.getSelectionModel().getSelectedItem();
		this.correlativeCourses.add(course);
	}

	public void removeCorrelativeCourse() {
		Course course = this.correlativeCourseList.getSelectionModel().getSelectedItem();
		this.correlativeCourses.remove(course);
	}

	private Course createCourse() {
		var newID = Integer.parseInt(this.idField.getText());
		var newName = this.nameField.getText();
		return new Course(newID, newName);
	}

	private void updateCourse(Course course) {
		course.setName(nameField.getText());
		course.setCoursePeriod(coursePeriodField.getValue());
		course.setYear(yearField.getValue());
		course.setCourseStatus(courseStatusField.getValue());
		course.setCourseType(courseTypeField.getValue());
		course.setHours(hoursField.getValue());
		course.setCreditos(creditsField.getValue());
		course.setCorrelatives(new HashSet<>(this.correlativeCourses));
		if (courseStatusField.getValue() == CourseStatus.APROBADA)
			course.setGrade(gradeField.getValue());
	}

	private void saveAndExit() {
		if (this.injectedCourse == null) {
			this.injectedCourse = createCourse();
			program.addCourse(injectedCourse);
		}
		else if (!idMatchesCourse(this.injectedCourse)) {
			var newCourse = createCourse();
			program.replaceCourse(injectedCourse, newCourse);
		}
		updateCourse(injectedCourse);
		this.mainController.emitUnsavedChanges();
		close();
	}

	public void close() {
		((Stage) this.cancelButton.getScene().getWindow()).close();
		this.subscriptions.dispose();
	}

	public void accept() {
		if (validator.validate())
			saveAndExit();
	}

}
