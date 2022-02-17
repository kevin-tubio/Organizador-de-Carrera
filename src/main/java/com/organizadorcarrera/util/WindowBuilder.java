package com.organizadorcarrera.util;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WindowBuilder {

	private Stage stage;
	private Modality modalidad;
	private Consumer<FXMLLoader> funcion;
	private Scene scene;
	private FXMLLoader loader;
	private BiConsumer<Stage, FXMLLoader> biFuncion;

	public WindowBuilder() {
		this.stage = new Stage();
		this.modalidad = Modality.NONE;
		this.scene = new Scene(new AnchorPane());
	}

	public void setFXMLScene(String url) {
		this.loader = new FXMLLoader(this.getClass().getResource(url), LangResource.getResourceBundle());
		try {
			this.scene = new Scene(this.loader.load());
		} catch (IOException e) {
			this.loader = null;
			return;
		}
		if (this.funcion != null)
			establecerFuncion(this.funcion);
		if (this.biFuncion != null)
			establecerFuncion(this.biFuncion);
	}

	public void establecerFuncion(Consumer<FXMLLoader> funcion) {
		this.funcion = funcion;
		if (loader != null)
			funcion.accept(this.loader);
	}

	public void establecerFuncion(BiConsumer<Stage, FXMLLoader> funcion) {
		this.biFuncion = funcion;
		if (loader != null)
			funcion.accept(this.stage, this.loader);
	}

	public void setTituloInternacionalizable(String claveDeTitulo) {
		this.stage.setTitle(LangResource.getString(claveDeTitulo));
	}

	public void setModalidad(Modality modalidad) {
		this.modalidad = modalidad;
	}

	public Stage construirVentana() {
		stage.initModality(modalidad);
		stage.setScene(scene);
		return this.stage;
	}

}
