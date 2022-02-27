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

	private ControladorPrincipal controladorPrincipal;

	public void guardar() {
		this.controladorPrincipal.persistirCambiosListado();
		cerrarConfirmacion();
		this.controladorPrincipal.cerrarVentana();
	}

	public void descartar() {
		cerrarConfirmacion();
		this.controladorPrincipal.cerrarVentana();
	}

	public void cancelar() {
		cerrarConfirmacion();
	}

	private void cerrarConfirmacion() {
		((Stage) this.botonCancelar.getScene().getWindow()).close();
	}

	@Autowired
	public void setControladorPrincipal(ControladorPrincipal controladorPrincipal) {
		this.controladorPrincipal = controladorPrincipal;
	}
}
