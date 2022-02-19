package com.organizadorcarrera;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

import com.organizadorcarrera.app.FXApplication;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		Application.launch(FXApplication.class, args);
	}
}
