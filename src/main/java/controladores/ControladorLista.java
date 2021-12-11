package controladores;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import listado.Listado;
import listado.Materia;
import util.Inyectable;

public class ControladorLista implements Initializable, Inyectable {

	private ControladorPrincipal controlador;
	private FilteredList<Materia> listaFiltrada;
	private ObservableList<Materia> listaDeMaterias;
	@FXML
	private ListView<Materia> lista;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.lista.setPlaceholder(new Label("No hay materias cargadas"));
		this.agregarSubscriptorAListado();
		this.listaFiltrada = new FilteredList<>(this.listaDeMaterias);
		this.lista.setItems(this.listaFiltrada);
	}

	private void agregarSubscriptorAListado() {
		this.listaDeMaterias = FXCollections.observableArrayList();
		MapChangeListener<Integer, Materia> subscriptor = cambio -> {
			if (cambio.wasAdded()) {
				this.listaDeMaterias.add(cambio.getValueAdded());
			} else {
				this.listaDeMaterias.remove(cambio.getValueRemoved());
			}
		};
		Listado.obtenerListado().getListadoDeMaterias().addListener(subscriptor);
	}

	protected Materia obtenerSeleccionado() {
		return this.lista.getSelectionModel().getSelectedItem();
	}

	public void habilitarFunciones() {
		if (this.obtenerSeleccionado() != null && !Listado.obtenerListado().getListadoDeMaterias().isEmpty()) {
			this.controlador.habilitarFunciones();
		}
	}

	@Override
	public void inyectarControlador(ControladorPrincipal controladorPrincipal) {
		this.controlador = controladorPrincipal;
	}
}
