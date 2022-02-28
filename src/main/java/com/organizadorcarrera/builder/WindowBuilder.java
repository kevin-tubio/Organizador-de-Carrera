package com.organizadorcarrera.builder;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.organizadorcarrera.util.LangResource;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class WindowBuilder {

	@Autowired
	private ApplicationContext applicationContext;

	private Stage stage;

	public void setFXMLScene(Stage stage, FXMLLoader loader) {
		this.stage = stage;
		stage.setScene(getScene(loader));
	}

	public void setFXMLScene(FXMLLoader loader) {
		this.setFXMLScene(new Stage(), loader);
	}

	private Scene getScene(FXMLLoader loader) {
		try {
			return new Scene(loader.load());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setLocaleTitle(String key) {
		stage.setTitle(LangResource.getString(key));
	}

	public void setModality(Modality modality) {
		stage.initModality(modality);
	}

	public FXMLLoader createLoader(String url) {
		var loader = new FXMLLoader(this.getClass().getResource(url), LangResource.getResourceBundle());
		loader.setControllerFactory(applicationContext::getBean);
		return loader;
	}

	public void showWindow() {
		stage.show();
	}

	public void showModalWindow() {
		setModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public void showModalSubWindow() {
		setModality(Modality.WINDOW_MODAL);
		stage.showAndWait();
	}

}
