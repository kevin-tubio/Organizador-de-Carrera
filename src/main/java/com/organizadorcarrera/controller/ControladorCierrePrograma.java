package com.organizadorcarrera.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControladorCierrePrograma {

	@FXML
	private Button botonCancelar;

	private MainController mainController;

	public void guardar() {
		this.mainController.persistirCambiosListado();
		cerrarConfirmacion();
		this.mainController.cerrarVentana();
	}

	public void descartar() {
		cerrarConfirmacion();
		this.mainController.cerrarVentana();
	}

	public void cancelar() {
		cerrarConfirmacion();
	}

	private void cerrarConfirmacion() {
		((Stage) this.botonCancelar.getScene().getWindow()).close();
	}

	@Autowired
	public void setControladorPrincipal(MainController controladorPrincipal) {
		this.mainController = controladorPrincipal;
	}
}
