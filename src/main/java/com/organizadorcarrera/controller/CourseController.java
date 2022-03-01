package com.organizadorcarrera.controller;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
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

	private ObservableList<Course> courses;
	private ObservableList<Course> correlativeCourses;
	private FilteredList<Course> filteredList;
	private Validator validator;
	private Course injectedCourse;
	private MainController mainController;

	public CourseController() {
		this.validator = new Validator();
		this.courses = FXCollections.observableArrayList(Program.getInstance().getProgramMap().values());
		this.correlativeCourses = FXCollections.observableArrayList();
		this.filteredList = new FilteredList<>(this.courses);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.initializeFields();
		this.createFieldsValidator();
		this.subscribeToSearchBox();
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
		this.gradeField.disableProperty()
				.bind(courseStatusField.getSelectionModel().selectedItemProperty().isNotEqualTo(CourseStatus.APROBADA));
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
			if (!this.idField.getText().matches("^[0-9]+$"))
				in.error(LangResource.getString("InputIdInvalido"));
			if (idIsRepeated())
				in.error(LangResource.getString("InputIdRepetido"));
			if (courseCicles())
				in.error(LangResource.getString("InputIdCiclo"));
		}).dependsOn("id", this.idField.textProperty()).decorates(this.idField);
	}

	private boolean idIsRepeated() {
		return !idMatchesCourse(this.injectedCourse)
				&& Program.getInstance().containsCourse(this.idField.getText());
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
		this.filteredList.setPredicate(course -> isNotFiltered(course, filter.toLowerCase()));
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
			if (Boolean.FALSE.equals(newValue)) {
				this.courseList.setVisible(false);
				if (!this.searchField.isFocused())
					this.searchField.clear();
			}
		});
	}

//----------------------------------------------------------------------------------------------------------------------

	@Autowired
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
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
		this.courses.removeAll(this.correlativeCourses);
	}

	public void addCorrelativeCourse() {
		Course course = this.courseList.getSelectionModel().getSelectedItem();
		this.correlativeCourses.add(course);
		this.courses.remove(course);
	}

	public void removeCorrelativeCourse() {
		Course course = this.correlativeCourseList.getSelectionModel().getSelectedItem();
		this.correlativeCourses.remove(course);
		this.courses.add(course);
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
		if (this.injectedCourse == null)
			this.injectedCourse = createCourse();
		if (!idMatchesCourse(this.injectedCourse)) {
			var newCourse = createCourse();
			updateCourse(newCourse);
			Program.getInstance().replaceCourse(injectedCourse, newCourse);
		} else {
			updateCourse(injectedCourse);
			Program.getInstance().addCourse(injectedCourse);
		}
		this.mainController.declararCambios();
		close();
	}

	public void close() {
		((Stage) this.cancelButton.getScene().getWindow()).close();
	}

	public void accept() {
		if (validator.validate())
			saveAndExit();
	}

}
