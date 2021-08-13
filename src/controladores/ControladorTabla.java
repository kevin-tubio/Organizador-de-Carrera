package controladores;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class ControladorTabla implements Initializable {
	private ControladorPrincipal controlador;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	public void inyectar(ControladorPrincipal controladorPrincipal) {
		this.controlador = controladorPrincipal;
	}
}
