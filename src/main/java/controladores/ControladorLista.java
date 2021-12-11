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

	@FXML
	private ListView<Materia> lista;

	private ControladorPrincipal controlador;
	private FilteredList<Materia> listaFiltrada;
	private ObservableList<Materia> listaDeMaterias;

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.lista.setPlaceholder(new Label(resourceBundle.getString("ListadoVacio")));
		this.agregarSubscriptorAListado();
		this.listaFiltrada = new FilteredList<>(this.listaDeMaterias);
		this.lista.setItems(this.listaFiltrada);
	}

	private void agregarSubscriptorAListado() {
		this.listaDeMaterias = FXCollections.observableArrayList();
		MapChangeListener<Integer, Materia> subscriptor = cambio -> this.listaDeMaterias
				.setAll(cambio.getMap().values());
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
