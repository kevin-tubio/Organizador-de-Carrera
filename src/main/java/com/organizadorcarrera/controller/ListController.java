package com.organizadorcarrera.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import com.organizadorcarrera.entity.Course;
import com.organizadorcarrera.program.Program;

@Component
public class ListController implements Initializable {

	@FXML
	private ListView<Course> listView;

	private MainController mainController;
	private FilteredList<Course> filteredList;
	private ObservableList<Course> courseList;

	public ListController() {
		this.courseList = FXCollections.observableArrayList();
		this.filteredList = new FilteredList<>(this.courseList);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.listView.setPlaceholder(new Label(resourceBundle.getString("ListadoVacio")));
		this.subscribeToProgram();
		this.listView.setItems(this.filteredList);
	}

	private void subscribeToProgram() {
		MapChangeListener<Integer, Course> listener = change -> this.courseList.setAll(change.getMap().values());
		Program.getInstance().getProgramMap().addListener(listener);
	}

	protected Course getSelectedItem() {
		return this.listView.getSelectionModel().getSelectedItem();
	}

	public void enableActions() {
		if (this.getSelectedItem() != null && !Program.getInstance().getProgramMap().isEmpty()) {
			this.mainController.enableActions();
		}
	}

	@Autowired
	public void setMainController(MainController controladorPrincipal) {
		this.mainController = controladorPrincipal;
	}
}
