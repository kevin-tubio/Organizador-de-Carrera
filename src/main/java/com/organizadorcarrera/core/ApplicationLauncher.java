package com.organizadorcarrera.core;

import com.organizadorcarrera.core.FXApplication.FXReadyEvent;
import com.organizadorcarrera.util.WindowDirector;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationLauncher implements ApplicationListener<FXReadyEvent> {

	private final WindowDirector windowDirector;
	private final Logger logger = LoggerFactory.getLogger(ApplicationLauncher.class);

	@Override
	public void onApplicationEvent(FXReadyEvent event) {
		this.windowDirector.showMainWindow(event.getSource());
		logger.info("JavaFX application initialized");
	}

}
