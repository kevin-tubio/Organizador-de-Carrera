package com.organizadorcarrera.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DirectorVentana {

	private WindowBuilder builder;

	public DirectorVentana(String url, String claveDeTitulo) {
		this(url, claveDeTitulo, loader -> {
		});
	}

	public DirectorVentana(String url, String claveDeTitulo, Consumer<FXMLLoader> funcion) {
		this(url, claveDeTitulo, (stage, loader) -> funcion.accept(loader));
	}

	public DirectorVentana(String url, String claveDeTitulo, BiConsumer<Stage, FXMLLoader> funcion) {
		this.builder = new WindowBuilder();
		this.builder.setTituloInternacionalizable(claveDeTitulo);
		this.builder.setFXMLScene(url);
		this.builder.establecerFuncion(funcion);
	}

	public void hacerVentana() {
		builder.construirVentana().show();
	}

	public void hacerVentanaModal() {
		this.builder.setModalidad(Modality.APPLICATION_MODAL);
		builder.construirVentana().showAndWait();
	}

	public void hacerSubVentanaModal() {
		this.builder.setModalidad(Modality.WINDOW_MODAL);
		builder.construirVentana().showAndWait();
	}

}
