package com.organizadorcarrera.controller;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class ExitController implements Initializable {

	@FXML
	private Button saveChangesButton;

	@FXML
	private Button ignoreChangesButton;

	@FXML
	private Button cancelButton;

	private final MainController mainController;
	private final CompositeDisposable subscriptions;

	@Override
	public void initialize(URL arg0, ResourceBundle resources) {
		this.subscribeToEvents();
	}

	private void subscribeToEvents() {
		subscriptions.addAll(
				JavaFxObservable.actionEventsOf(this.saveChangesButton).subscribe(onClick -> this.saveChanges()),
				JavaFxObservable.actionEventsOf(this.ignoreChangesButton).subscribe(onClick -> this.ignoreChanges()),
				JavaFxObservable.actionEventsOf(this.cancelButton).subscribe(onClick -> this.cancel())
		);
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
		this.subscriptions.clear();
	}

}
