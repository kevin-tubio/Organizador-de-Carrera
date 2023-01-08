package com.organizadorcarrera.controller;

import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.service.ProgramService;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class ListController implements Initializable {

	@FXML
	private ListView<Course> listView;

	private final MainController mainController;
	private final FilteredList<Course> filteredList;
	private final ProgramService programService;

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.listView.setPlaceholder(new Label(resourceBundle.getString("ListadoVacio")));
		this.listView.setItems(this.filteredList);
	}

	protected Course getSelectedItem() {
		return this.listView.getSelectionModel().getSelectedItem();
	}

	public void enableActions() {
		if (this.getSelectedItem() != null && !programService.isEmpty()) {
			this.mainController.enableActions();
		}
	}

}
