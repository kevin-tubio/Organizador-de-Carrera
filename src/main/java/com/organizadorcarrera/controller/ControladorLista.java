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

import com.organizadorcarrera.entity.Materia;
import com.organizadorcarrera.listado.Listado;

@Component
public class ControladorLista implements Initializable {

	@FXML
	private ListView<Materia> lista;

	private ControladorPrincipal controladorPrincipal;
	private FilteredList<Materia> listaFiltrada;
	private ObservableList<Materia> listaDeMaterias;

	public ControladorLista() {
		this.listaDeMaterias = FXCollections.observableArrayList();
		this.listaFiltrada = new FilteredList<>(this.listaDeMaterias);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.lista.setPlaceholder(new Label(resourceBundle.getString("ListadoVacio")));
		this.agregarSubscriptorAListado();
		this.lista.setItems(this.listaFiltrada);
	}

	private void agregarSubscriptorAListado() {
		MapChangeListener<Integer, Materia> subscriptor = cambio -> this.listaDeMaterias
				.setAll(cambio.getMap().values());
		Listado.obtenerListado().getListadoDeMaterias().addListener(subscriptor);
	}

	protected Materia obtenerSeleccionado() {
		return this.lista.getSelectionModel().getSelectedItem();
	}

	public void habilitarFunciones() {
		if (this.obtenerSeleccionado() != null && !Listado.obtenerListado().getListadoDeMaterias().isEmpty()) {
			this.controladorPrincipal.habilitarFunciones();
		}
	}

	@Autowired
	public void setControladorPrincipal(ControladorPrincipal controladorPrincipal) {
		this.controladorPrincipal = controladorPrincipal;
	}
}
