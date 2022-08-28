package com.organizadorcarrera.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.program.Program;

@Component
public class ListController implements Initializable {

	@FXML
	private ListView<Course> listView;

	private final MainController mainController;
	private final FilteredList<Course> filteredList;
	private final Program program;

	@Autowired
	public ListController(MainController mainController, Program program, FilteredList<Course> filteredCourseList) {
		this.filteredList = filteredCourseList;
		this.program = program;
		this.mainController = mainController;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.listView.setPlaceholder(new Label(resourceBundle.getString("ListadoVacio")));
		this.listView.setItems(this.filteredList);
	}

	protected Course getSelectedItem() {
		return this.listView.getSelectionModel().getSelectedItem();
	}

	public void enableActions() {
		if (this.getSelectedItem() != null && !program.isEmpty()) {
			this.mainController.enableActions();
		}
	}

}
