package controladores;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import listado.Listado;
import listado.Materia;

public class ControladorTabla implements Initializable {

	private ControladorPrincipal controlador;

	@FXML
	private TableView<Materia> tabla;
	@FXML
	private TableColumn<Materia, Integer> numeroActividad;
	@FXML
	private TableColumn<Materia, String> nombreActividad;
	@FXML
	private TableColumn<Materia, Integer> anio;
	@FXML
	private TableColumn<Materia, String> periodo;
	@FXML
	private TableColumn<Materia, String> nota;
	@FXML
	private TableColumn<Materia, String> estado;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.inicializarTabla();
		this.agregarSubscriptorAListado();
	}

	private void inicializarTabla() {
		numeroActividad.setCellValueFactory(new PropertyValueFactory<>("numeroActividad"));
		nombreActividad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		anio.setCellValueFactory(new PropertyValueFactory<>("anio"));
		periodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
		nota.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
		estado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		tabla.setPlaceholder(new Label("No hay materias cargadas"));
	}

	private void agregarSubscriptorAListado() {
		MapChangeListener<Integer, Materia> subscriptor = cambio -> {
			if (cambio.wasAdded()) {
				this.tabla.getItems().add(cambio.getValueAdded());
			} else {
				this.tabla.getItems().remove(cambio.getValueRemoved());
			}
		};
		Listado.obtenerListado().getListadoDeMaterias().addListener(subscriptor);
	}

	public void inyectar(ControladorPrincipal controladorPrincipal) {
		this.controlador = controladorPrincipal;
	}
}
