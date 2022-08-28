package com.organizadorcarrera.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.organizadorcarrera.core.FXApplication.FXReadyEvent;
import com.organizadorcarrera.util.WindowDirector;

@Component
public class ApplicationLauncher implements ApplicationListener<FXReadyEvent> {

	private final Logger logger;
	private final WindowDirector windowDirector;

	@Autowired
	public ApplicationLauncher(WindowDirector windowDirector) {
		this.logger = LoggerFactory.getLogger(ApplicationLauncher.class);
		this.windowDirector = windowDirector;
	}

	@Override
	public void onApplicationEvent(FXReadyEvent event) {
		this.windowDirector.showMainWindow(event.getSource());
		logger.info("JavaFX application initialized");
	}
}
