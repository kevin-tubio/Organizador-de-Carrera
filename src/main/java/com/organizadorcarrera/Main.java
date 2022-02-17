package com.organizadorcarrera.app;

import java.io.IOException;
import java.util.function.BiConsumer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import com.organizadorcarrera.util.DirectorVentana;
import com.organizadorcarrera.controladores.ControladorPrincipal;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		BiConsumer<Stage, FXMLLoader> funcion = (stageVentana, loader) -> stageVentana
				.setOnCloseRequest(((ControladorPrincipal) loader.getController())::cerrarPrograma);
		new DirectorVentana("/fxml/Main.fxml", "TituloVentanaPrincipal", funcion).hacerVentana();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
