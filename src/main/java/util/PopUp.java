package util;

import java.io.IOException;

import controladores.ControladorPrincipal;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PopUp<C extends Inyectable> {

	public void mostrarPopUp(String url, String titulo, ControladorPrincipal controlador) {
		mostrarPopUp(url, titulo, controlador, loader -> {
		});
	}

	public void mostrarPopUp(String url, String titulo, ControladorPrincipal controlador, FuncionPopUp<C> funcion) {
		var stage = new Stage();
		try {
			var loader = new FXMLLoader(this.getClass().getResource(url));
			var scene = new Scene(loader.load());
			C controladorVentana = loader.getController();
			controladorVentana.inyectarControlador(controlador);
			funcion.ejecutar(controladorVentana);
			stage.setTitle(titulo);
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
