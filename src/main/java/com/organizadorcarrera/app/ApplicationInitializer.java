package com.organizadorcarrera.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import com.organizadorcarrera.app.FXApplication.FXReadyEvent;
import com.organizadorcarrera.controladores.ControladorPrincipal;
import com.organizadorcarrera.util.LangResource;

@Component
public class ApplicationInitializer implements ApplicationListener<FXReadyEvent> {

	@Value("classpath:/fxml/Main.fxml")
	private Resource resource;

	private final Logger logger;
	private final ApplicationContext applicationContext;
	
	public ApplicationInitializer(ApplicationContext applicationContext) {
		this.logger = LoggerFactory.getLogger(ApplicationInitializer.class);
		this.applicationContext = applicationContext;
	}

	@Override
	public void onApplicationEvent(FXReadyEvent event) {
		logger.info("Application Ready");
		var stage = event.getSource();
		var loader = getLoader();
		loader.setControllerFactory(this.applicationContext::getBean);
		stage.setTitle(LangResource.getString("TituloVentanaPrincipal"));
		stage.setScene(getScene(loader));
		stage.setOnCloseRequest(((ControladorPrincipal) loader.getController())::cerrarPrograma);
		stage.show();
	}

	private FXMLLoader getLoader() {
		try {
			return new FXMLLoader(this.resource.getURL(), LangResource.getResourceBundle());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Scene getScene(FXMLLoader loader) {
		try {
			return new Scene(loader.load());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
