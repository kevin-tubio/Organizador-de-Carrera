package com.organizadorcarrera.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.organizadorcarrera.app.FXApplication.FXReadyEvent;
import com.organizadorcarrera.util.DirectorVentana;

@Component
public class ApplicationInitializer implements ApplicationListener<FXReadyEvent> {

	private final Logger logger;

	@Autowired
	private DirectorVentana directorVentana;

	public ApplicationInitializer() {
		this.logger = LoggerFactory.getLogger(ApplicationInitializer.class);
	}

	@Override
	public void onApplicationEvent(FXReadyEvent event) {
		this.directorVentana.hacerVentanaMain(event.getSource());
		logger.info("JavaFX application initialized");
	}
}
