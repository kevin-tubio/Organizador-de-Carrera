package com.organizadorcarrera.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
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
	private MenuItem saveChangesMenuItem;

	@FXML
	private MenuItem exitAppMenuItem;

	@FXML
	private AnchorPane anchor;

	@Autowired
	private WindowDirector windowDirector;

	@Autowired
	private ListadoService materiaService;

	private SimpleBooleanProperty cambiosSubject;
	private Logger logger;

	public MainController() {
		this.cambiosSubject = new SimpleBooleanProperty(false);
		this.logger = LoggerFactory.getLogger(MainController.class);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.saveChangesMenuItem.disableProperty().bind(this.cambiosSubject.not());
		this.disableActions();
		materiaService.recuperarListado();
		tableController.loadTableConfiguration();
	}

	public void openFile() {
		var fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"),
				new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		var archivo = fileChooser.showOpenDialog(new Stage());
		if (archivo != null) {
			Program.clearProgram();
			Platform.runLater(() -> {
				try {
					parseCourses(archivo);
				} catch (FileException e) {
					logger.debug(e.getMessage(), e);
				}
			});
			declararCambios();
		}
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
		declararCambios();
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
		this.cambiosSubject.set(false);
	}

	public void declararCambios() {
		this.cambiosSubject.set(true);
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
		return this.cambiosSubject.get();
	}

	private void showExitWindow() {
		this.windowDirector.showExitWindow();
	}

	void exit() {
		Platform.exit();
	}
}
