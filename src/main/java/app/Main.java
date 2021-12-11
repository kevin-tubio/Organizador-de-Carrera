package app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import controladores.ControladorPrincipal;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		var loader = new FXMLLoader(this.getClass().getResource("../fxml/Main.fxml"));
		var scene = new Scene(loader.load());
		stage.setTitle("Organizador");
		stage.setScene(scene);
		stage.setOnCloseRequest(((ControladorPrincipal) loader.getController())::cerrarPrograma);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
