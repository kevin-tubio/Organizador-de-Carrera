package com.organizadorcarrera.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import com.organizadorcarrera.util.Inyectable;

public class ControladorCierrePrograma implements Inyectable {

	@FXML
	private Button botonCancelar;

	private ControladorPrincipal controlador;

	public void guardar() {
		this.controlador.persistirCambiosListado();
		cerrarConfirmacion();
		this.controlador.cerrarVentana();
	}

	public void descartar() {
		cerrarConfirmacion();
		this.controlador.cerrarVentana();
	}

	public void cancelar() {
		cerrarConfirmacion();
	}

	private void cerrarConfirmacion() {
		((Stage) this.botonCancelar.getScene().getWindow()).close();
	}

	@Override
	public void inyectarControlador(ControladorPrincipal controladorPrincipal) {
		this.controlador = controladorPrincipal;
	}
}
