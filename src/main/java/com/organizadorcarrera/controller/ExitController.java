package com.organizadorcarrera.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExitController {

	@FXML
	private Button cancelButton;

	private MainController mainController;

	public void saveChanges() {
		this.mainController.saveChanges();
		closeWindow();
		this.mainController.exit();
	}

	public void ignoreChanges() {
		closeWindow();
		this.mainController.exit();
	}

	public void cancel() {
		closeWindow();
	}

	private void closeWindow() {
		((Stage) this.cancelButton.getScene().getWindow()).close();
	}

	@Autowired
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
}
