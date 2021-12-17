package util;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
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
	private String url;
	private ResourceBundle resourceBundle;
	private Consumer<FXMLLoader> funcion;
	private Scene scene;
	private FXMLLoader loader;
	private BiConsumer<Stage, FXMLLoader> biFuncion;

	public WindowBuilder() {
		this.stage = new Stage();
		this.modalidad = Modality.NONE;
		this.scene = new Scene(new AnchorPane());
		setResourceBundle(Locale.getDefault());
	}

	public void setResourceBundle(Locale locale) {
		try {
			this.resourceBundle = ResourceBundle.getBundle("lang.string", locale);
		} catch (MissingResourceException e) {
			this.resourceBundle = ResourceBundle.getBundle("lang.string", new Locale("en"));
		}
		if (loader != null)
			setFXMLScene(this.url);
	}

	public void setFXMLScene(String url) {
		this.url = url;
		this.loader = new FXMLLoader(this.getClass().getResource(url), this.resourceBundle);
		try {
			this.scene = new Scene(this.loader.load());
		} catch (IOException e) {
			this.url = null;
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
		try {
			this.stage.setTitle(resourceBundle.getString(claveDeTitulo));
		} catch (MissingResourceException e) {
			this.stage.setTitle("");
		}
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
