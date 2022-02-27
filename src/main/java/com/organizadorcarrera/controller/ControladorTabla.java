package com.organizadorcarrera.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import com.organizadorcarrera.config.TableConfiguration;
import com.organizadorcarrera.entity.Materia;
import com.organizadorcarrera.enumerados.Estado;
import com.organizadorcarrera.enumerados.Periodo;
import com.organizadorcarrera.enumerados.Tipo;
import com.organizadorcarrera.enumerados.TipoConfiguracion;
import com.organizadorcarrera.listado.Listado;
import com.organizadorcarrera.service.ConfigurationService;

@Component
public class ControladorTabla implements Initializable {

	@FXML
	private TableView<Materia> tabla;

	@FXML
	private TableColumn<Materia, Integer> numeroActividad;

	@FXML
	private TableColumn<Materia, String> nombreActividad;

	@FXML
	private TableColumn<Materia, Integer> anio;

	@FXML
	private TableColumn<Materia, Periodo> periodo;

	@FXML
	private TableColumn<Materia, String> nota;

	@FXML
	private TableColumn<Materia, Estado> estado;

	@FXML
	private TableColumn<Materia, Tipo> tipo;

	@FXML
	private TableColumn<Materia, Integer> hs;

	@FXML
	private TableColumn<Materia, Double> creditos;

	@FXML
	private MenuItem itemContextualEditar;

	@FXML
	private MenuItem itemContextualBorrar;

	@Autowired
	private ConfigurationService configurationService;

	private ControladorPrincipal controladorPrincipal;

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.inicializarTabla(resourceBundle);
		this.agregarSubscriptorAListado();
		this.itemContextualEditar.disableProperty().bind(tabla.getSelectionModel().selectedItemProperty().isNull());
		this.itemContextualBorrar.disableProperty().bind(tabla.getSelectionModel().selectedItemProperty().isNull());
	}

	private void inicializarTabla(ResourceBundle resourceBundle) {
		numeroActividad.setCellValueFactory(new PropertyValueFactory<>("numeroActividad"));
		nombreActividad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		anio.setCellValueFactory(new PropertyValueFactory<>("anio"));
		periodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
		nota.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
		estado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		hs.setCellValueFactory(new PropertyValueFactory<>("horasSemanales"));
		creditos.setCellValueFactory(new PropertyValueFactory<>("creditos"));
		establecerComparadoresColumnas();
		tabla.setPlaceholder(new Label(resourceBundle.getString("ListadoVacio")));
	}

	private void establecerComparadoresColumnas() {
		periodo.setComparator((o1, o2) -> o1.toString().compareTo(o2.toString()));
		estado.setComparator((o1, o2) -> o1.toString().compareTo(o2.toString()));
		tipo.setComparator((o1, o2) -> o1.toString().compareTo(o2.toString()));
		nota.setComparator((o1, o2) -> {
			if (o1.matches("\\d+") && o2.matches("\\d+"))
				return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
			return o1.compareTo(o2);
		});
	}

	private void agregarSubscriptorAListado() {
		MapChangeListener<Integer, Materia> subscriptor = cambio -> tabla.getItems().setAll(cambio.getMap().values());
		Listado.obtenerListado().getListadoDeMaterias().addListener(subscriptor);
	}

	public void habilitarFunciones() {
		if (this.obtenerSeleccionado() != null) {
			this.controladorPrincipal.habilitarFunciones();
		}
	}

	protected Materia obtenerSeleccionado() {
		return this.tabla.getSelectionModel().getSelectedItem();
	}

	public void borrarContextual() {
		this.controladorPrincipal.borrarMateria(this.obtenerSeleccionado());
	}

	public void editarContextual() {
		this.controladorPrincipal.editarMateria(this.obtenerSeleccionado());
		tabla.refresh();
	}

	public void agregarMateria() {
		this.controladorPrincipal.agregarMateria();
	}

	public void guardarDimensionesTabla() {
		TableConfiguration configuracion = new TableConfiguration();
		guardarDimensiones(configuracion);
		guardarVisibles(configuracion);
		configurationService.save(configuracion.getConfig());
	}

	private void guardarDimensiones(TableConfiguration configuracion) {
		configuracion.setAnchoColumnaId(this.numeroActividad.getWidth());
		configuracion.setAnchoColumnaNombre(this.nombreActividad.getWidth());
		configuracion.setAnchoColumnaAnio(this.anio.getWidth());
		configuracion.setAnchoColumnaPeriodo(this.periodo.getWidth());
		configuracion.setAnchoColumnaNota(this.nota.getWidth());
		configuracion.setAnchoColumnaEstado(this.estado.getWidth());
		configuracion.setAnchoColumnaTipo(this.tipo.getWidth());
		configuracion.setAnchoColumnaHS(this.hs.getWidth());
		configuracion.setAnchoColumnaCreditos(this.creditos.getWidth());
	}

	private void guardarVisibles(TableConfiguration configuracion) {
		configuracion.setIdVisible(this.numeroActividad.isVisible());
		configuracion.setNombreVisible(this.nombreActividad.isVisible());
		configuracion.setAnioVisible(this.anio.isVisible());
		configuracion.setPeriodoVisible(this.periodo.isVisible());
		configuracion.setNotaVisible(this.nota.isVisible());
		configuracion.setEstadoVisible(this.estado.isVisible());
		configuracion.setTipoVisible(this.tipo.isVisible());
		configuracion.setHSVisible(this.hs.isVisible());
		configuracion.setCreditosVisible(this.creditos.isVisible());
	}

	public void recuperarDimensionesTabla() {
		TableConfiguration configuracion = new TableConfiguration(configurationService.findByTipoConfiguracion(TipoConfiguracion.TABLA));
		if (configuracion.esValida()) {
			dimensionarColumnas(configuracion);
			recuperarVisibles(configuracion);
		}
	}

	private void dimensionarColumnas(TableConfiguration config) {
		this.numeroActividad.setPrefWidth(config.getAnchoColumnaId());
		this.nombreActividad.setPrefWidth(config.getAnchoColumnaNombre());
		this.anio.setPrefWidth(config.getAnchoColumnaAnio());
		this.periodo.setPrefWidth(config.getAnchoColumnaPeriodo());
		this.nota.setPrefWidth(config.getAnchoColumnaNota());
		this.estado.setPrefWidth(config.getAnchoColumnaEstado());
		this.tipo.setPrefWidth(config.getAnchoColumnaTipo());
		this.hs.setPrefWidth(config.getAnchoColumnaHS());
		this.creditos.setPrefWidth(config.getAnchoColumnaCreditos());
	}

	private void recuperarVisibles(TableConfiguration configuracion) {
		this.numeroActividad.setVisible(configuracion.getIdVisible());
		this.nombreActividad.setVisible(configuracion.getNombreVisible());
		this.anio.setVisible(configuracion.getAnioVisible());
		this.periodo.setVisible(configuracion.getPeriodoVisible());
		this.nota.setVisible(configuracion.getNotaVisible());
		this.estado.setVisible(configuracion.getEstadoVisible());
		this.tipo.setVisible(configuracion.getTipoVisible());
		this.hs.setVisible(configuracion.getHSVisible());
		this.creditos.setVisible(configuracion.getCreditosVisible());
	}

	@Autowired
	public void setControladorPrincipal(ControladorPrincipal controladorPrincipal) {
		this.controladorPrincipal = controladorPrincipal;
	}
}
