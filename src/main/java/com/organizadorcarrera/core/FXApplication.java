package com.organizadorcarrera.core;

import com.organizadorcarrera.OrganizadorApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.stage.Stage;

public class FXApplication extends Application {

	private ConfigurableApplicationContext applicationContext;

	@Override
	public void init() {
		String[] args = getParameters().getRaw().toArray(new String[0]);
		this.applicationContext = new SpringApplicationBuilder(OrganizadorApplication.class).run(args);
	}

	@Override
	public void start(Stage stage) {
		this.applicationContext.publishEvent(new FXReadyEvent(stage));
	}

	@Override
	public void stop() {
		this.applicationContext.close();
	}

	static class FXReadyEvent extends ApplicationEvent {

		private static final long serialVersionUID = -485673839704469584L;

		public FXReadyEvent(Stage stage) {
			super(stage);
		}

		@Override
		public Stage getSource() {
			return (Stage) super.getSource();
		}

	}

}
