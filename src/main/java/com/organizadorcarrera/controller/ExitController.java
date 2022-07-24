package com.organizadorcarrera.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.reactivex.rxjavafx.observables.JavaFxObservable;

@Component
public class ExitController implements Initializable {

	@FXML
	private Button saveChangesButton;

	@FXML
	private Button ignoreChangesButton;

	@FXML
	private Button cancelButton;

	private MainController mainController;

	@Override
	public void initialize(URL arg0, ResourceBundle resources) {
		this.subscribeToEvents();
	}

	private void subscribeToEvents() {
		JavaFxObservable.actionEventsOf(this.saveChangesButton).subscribe(onClick -> this.saveChanges());
		JavaFxObservable.actionEventsOf(this.ignoreChangesButton).subscribe(onClick -> this.ignoreChanges());
		JavaFxObservable.actionEventsOf(this.cancelButton).subscribe(onClick -> this.cancel());
	}

	private void saveChanges() {
		this.mainController.saveChanges();
		closeWindow();
		this.mainController.exit();
	}

	private void ignoreChanges() {
		closeWindow();
		this.mainController.exit();
	}

	private void cancel() {
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
