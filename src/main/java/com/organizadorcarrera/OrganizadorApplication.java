package com.organizadorcarrera;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

import com.organizadorcarrera.core.FXApplication;

@SpringBootApplication
public class OrganizadorApplication {

	public static void main(String[] args) {
		Application.launch(FXApplication.class, args);
	}

}
