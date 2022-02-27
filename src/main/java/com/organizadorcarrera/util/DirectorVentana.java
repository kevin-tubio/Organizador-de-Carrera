package com.organizadorcarrera.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.organizadorcarrera.controller.CourseController;
import com.organizadorcarrera.controller.MainController;
import com.organizadorcarrera.entity.Materia;

import javafx.stage.Stage;

@Component
public class DirectorVentana {

	@Autowired
	private WindowBuilder windowBuilder;

	public void hacerVentanaMain(Stage stage) {
		var loader = this.windowBuilder.createLoader("/fxml/Main.fxml");
		this.windowBuilder.setFXMLScene(stage, loader);
		this.windowBuilder.setTituloInternacionalizable("TituloVentanaPrincipal");
		stage.setOnCloseRequest(((MainController) loader.getController())::cerrarPrograma);
		this.windowBuilder.hacerVentana();
	}

	public void hacerVentanaAgregarMateria() {
		this.windowBuilder.setFXMLScene(this.windowBuilder.createLoader("/fxml/AgregarMateria.fxml"));
		this.windowBuilder.setTituloInternacionalizable("TituloVentanaAgregar");
		this.windowBuilder.hacerVentanaModal();
	}

	public void hacerVentanaEditarMateria(Materia materia) {
		var loader = this.windowBuilder.createLoader("/fxml/AgregarMateria.fxml");
		this.windowBuilder.setFXMLScene(loader);
		this.windowBuilder.setTituloInternacionalizable("TituloVentanaEditar");
		CourseController controller = loader.getController();
		controller.inyectarMateria(materia);
		this.windowBuilder.hacerVentanaModal();
	}

	public void hacerVentanaCierre() {
		this.windowBuilder.setFXMLScene(this.windowBuilder.createLoader("/fxml/ConfirmacionCierre.fxml"));
		this.windowBuilder.setTituloInternacionalizable("TituloVentanaConfirmarCierre");
		this.windowBuilder.hacerVentanaModal();
	}

}
