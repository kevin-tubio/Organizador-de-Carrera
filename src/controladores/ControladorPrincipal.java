package controladores;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ControladorPrincipal implements Initializable {

	@FXML
	private ControladorTabla planDeEstudiosController;
	@FXML
	private ControladorLista listaDeMateriasController;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.planDeEstudiosController.inyectar(this);
		this.listaDeMateriasController.inyectar(this);
	}

}