package com.organizadorcarrera.builder;

import java.io.IOException;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.organizadorcarrera.util.LangResource;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

@Component
public class StageBuilder {

	private final ApplicationContext applicationContext;

	@Autowired
	public StageBuilder(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	private Stage stage;
	private FXMLLoader fxmlLoader;

	public StageBuilder init() {
		this.stage = new Stage();
		return this;
	}

	public StageBuilder initFrom(Stage stage) {
		this.stage = stage;
		return this;
	}

	public StageBuilder setFXMLScene(String fxmlPath) {
		fxmlLoader = new FXMLLoader(this.getClass().getResource(fxmlPath), LangResource.getResourceBundle());
		fxmlLoader.setControllerFactory(applicationContext::getBean);
		this.stage.setScene(getScene(fxmlLoader));
		return this;
	}

	public FXMLLoader getLoader() {
		return this.fxmlLoader;
	}

	private Scene getScene(FXMLLoader loader) {
		try {
			return new Scene(loader.load());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public StageBuilder withTitle(String titleKey) {
		this.stage.setTitle(LangResource.getString(titleKey));
		return this;
	}

	public StageBuilder asWindowModal() {
		return this.setModality(Modality.WINDOW_MODAL);
	}

	public StageBuilder asApplicationModal() {
		return this.setModality(Modality.APPLICATION_MODAL);
	}

	public StageBuilder setModality(Modality modality) {
		this.stage.initModality(modality);
		return this;
	}

	public StageBuilder doOnCloseRequest(EventHandler<WindowEvent> onCloseRequestHandler) {
		stage.setOnCloseRequest(onCloseRequestHandler);
		return this;
	}

	public StageBuilder doWithController(Consumer<? extends Initializable> consumer) {
		consumer.accept(this.fxmlLoader.getController());
		return this;
	}
	
	public Stage build() {
		return this.stage;
	}

}
