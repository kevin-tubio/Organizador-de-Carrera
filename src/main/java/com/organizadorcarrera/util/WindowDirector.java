package com.organizadorcarrera.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.organizadorcarrera.builder.WindowBuilder;
import com.organizadorcarrera.controller.CourseController;
import com.organizadorcarrera.controller.MainController;
import com.organizadorcarrera.entity.Course;

import javafx.stage.Stage;

@Component
public class WindowDirector {

	@Autowired
	private WindowBuilder windowBuilder;

	public void showMainWindow(Stage stage) {
		var loader = this.windowBuilder.createLoader("/fxml/Main.fxml");
		this.windowBuilder.setFXMLScene(stage, loader);
		this.windowBuilder.setLocaleTitle("TituloVentanaPrincipal");
		stage.setOnCloseRequest(((MainController) loader.getController())::closeWindow);
		this.windowBuilder.showWindow();
	}

	public void showNewCourseWindow() {
		this.windowBuilder.setFXMLScene(this.windowBuilder.createLoader("/fxml/Course.fxml"));
		this.windowBuilder.setLocaleTitle("TituloVentanaAgregar");
		this.windowBuilder.showModalWindow();
	}

	public void showEditCourseWindow(Course materia) {
		var loader = this.windowBuilder.createLoader("/fxml/Course.fxml");
		this.windowBuilder.setFXMLScene(loader);
		this.windowBuilder.setLocaleTitle("TituloVentanaEditar");
		CourseController controller = loader.getController();
		controller.injectCourse(materia);
		this.windowBuilder.showModalWindow();
	}

	public void showExitWindow() {
		this.windowBuilder.setFXMLScene(this.windowBuilder.createLoader("/fxml/Exit.fxml"));
		this.windowBuilder.setLocaleTitle("TituloVentanaConfirmarCierre");
		this.windowBuilder.showModalWindow();
	}

}
