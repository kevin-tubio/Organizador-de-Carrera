package com.organizadorcarrera.controller;

import java.io.File;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import io.reactivex.disposables.CompositeDisposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.organizadorcarrera.entity.Course;
import com.organizadorcarrera.exception.FileException;
import com.organizadorcarrera.parser.ExcelFileParser;
import com.organizadorcarrera.parser.FileParser;
import com.organizadorcarrera.parser.TextFileParser;
import com.organizadorcarrera.program.Program;
import com.organizadorcarrera.service.ListadoService;
import com.organizadorcarrera.util.WindowDirector;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;

@Component
public class MainController implements Initializable {

	@FXML
	private TableController tableController;

	@FXML
	private ListController listController;

	@FXML
	private MenuItem addCourseMenuItem;

	@FXML
	private MenuItem deleteCourseMenuItem;

	@FXML
	private MenuItem editCourseMenuItem;

	@FXML
	private TabPane tabPane;

	@FXML
	private MenuItem newProgramMenuItem;

	@FXML
	private MenuItem openFileMenuItem;

	@FXML
	private MenuItem saveChangesMenuItem;

	@FXML
	private MenuItem exitAppMenuItem;

	@FXML
	private AnchorPane anchor;

	@Autowired
	private WindowDirector windowDirector;

	@Autowired
	private ListadoService materiaService;

	@Value("${main.initial-file-directory}")
	private String initialFileDirectory;

	private final SimpleBooleanProperty unsavedChangesSubject;
	private final Logger logger;
	private final CompositeDisposable subscriptions;

	public MainController() {
		this.unsavedChangesSubject = new SimpleBooleanProperty(false);
		this.logger = LoggerFactory.getLogger(MainController.class);
		this.subscriptions = new CompositeDisposable();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.saveChangesMenuItem.disableProperty().bind(this.unsavedChangesSubject.not());
		this.disableActions();
		materiaService.recuperarListado();
		tableController.loadTableConfiguration();
		subscribeToEvents();
	}

	private void subscribeToEvents() {
		subscriptions.addAll(
				JavaFxObservable.actionEventsOf(this.newProgramMenuItem).subscribe(onClick -> this.clearProgram()),
				JavaFxObservable.actionEventsOf(this.saveChangesMenuItem).subscribe(onClick -> this.saveChanges()),
				JavaFxObservable.actionEventsOf(this.openFileMenuItem).flatMap(this::openFile).subscribe(this::parseFile, error -> {}),
				JavaFxObservable.actionEventsOf(this.exitAppMenuItem).subscribe(this::closeWindow),
				JavaFxObservable.actionEventsOf(this.addCourseMenuItem).subscribe(onClick -> this.addCourse()),
				JavaFxObservable.actionEventsOf(this.editCourseMenuItem).subscribe(onClick -> this.editCourse()),
				JavaFxObservable.actionEventsOf(this.deleteCourseMenuItem).subscribe(onClick -> this.deleteCourse())
		);
	}

	private Observable<File> openFile(ActionEvent event) {
		var fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"),
				new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		if (!this.initialFileDirectory.isBlank())
			fileChooser.setInitialDirectory(new File(this.initialFileDirectory));

		var file = fileChooser.showOpenDialog(new Stage());
		if (file == null)
			return Observable.error(new NoSuchFileException(""));

		return Observable.just(file);
	}

	private void parseFile(File file) {
		if (file == null)
			return;

		this.clearProgram();
		Platform.runLater(() -> {
			try {
				parseCourses(file);
			} catch (FileException e) {
				logger.debug(e.getMessage(), e);
			}
		});
	}

	private void clearProgram() {
		Program.clearProgram();
		emitUnsavedChanges();
	}

	private void parseCourses(File file) throws FileException {
		FileParser parser = null;
		if (file.getName().endsWith(".txt")) {
			parser = new TextFileParser();
		} else {
			parser = new ExcelFileParser();
		}
		parser.generarListado(file.getAbsolutePath());
	}

	public void enableActions() {
		this.setActionEnabled(true);
	}

	public void disableActions() {
		this.setActionEnabled(false);
	}

	private void setActionEnabled(boolean value) {
		this.editCourseMenuItem.setDisable(!value);
		this.deleteCourseMenuItem.setDisable(!value);
	}

	public void deleteCourse() {
		ejecutarSegunSeleccionado(this::deletCourse);
	}

	protected void deletCourse(Course selected) {
		Program.getInstance().deleteCourse(selected);
		emitUnsavedChanges();
	}

	public void addCourse() {
		this.windowDirector.showNewCourseWindow();
	}

	public void editCourse() {
		ejecutarSegunSeleccionado(this::editCourse);
	}

	private void ejecutarSegunSeleccionado(Consumer<Course> function) {
		switch (getTabLocalizedId()) {
		case "table":
			function.accept(tableController.getSelectedItem());
			break;
		case "list":
			function.accept(listController.getSelectedItem());
			break;
		default:
		}
		if (Program.getInstance().getProgramMap().isEmpty()) {
			this.disableActions();
		}
	}

	private String getTabLocalizedId() {
		return this.tabPane.getSelectionModel().getSelectedItem().getContent().getId();
	}

	protected void editCourse(Course course) {
		this.windowDirector.showEditCourseWindow(course);
	}

	public void saveChanges() {
		materiaService.persistirCambiosListado();
		this.unsavedChangesSubject.set(false);
	}

	public void emitUnsavedChanges() {
		this.unsavedChangesSubject.set(true);
	}

	public void closeWindow(Event closeEvent) {
		closeEvent.consume();
		this.tableController.saveTableConfiguration();
		if (!shouldSaveChanges())
			exit();
		else
			showExitWindow();
	}

	private boolean shouldSaveChanges() {
		return this.unsavedChangesSubject.get();
	}

	private void showExitWindow() {
		this.windowDirector.showExitWindow();
	}

	void exit() {
		Platform.exit();
	}
}
