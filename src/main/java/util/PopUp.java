package util;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import controladores.ControladorPrincipal;

public class PopUp<C extends Inyectable> {

	public void mostrarPopUp(String url, String titulo, ControladorPrincipal controlador) {
		mostrarPopUp(url, titulo, controlador, loader -> {
		});
	}

	public void mostrarPopUp(String url, String titulo, ControladorPrincipal controlador, Consumer<C> funcion) {
		var stage = new Stage();
		try {
			var resourceBundle = ResourceBundle.getBundle("lang.string", Locale.getDefault());
			var loader = new FXMLLoader(this.getClass().getResource(url), resourceBundle);
			var scene = new Scene(loader.load());
			C controladorVentana = loader.getController();
			controladorVentana.inyectarControlador(controlador);
			funcion.accept(controladorVentana);
			stage.setTitle(resourceBundle.getString(titulo));
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
