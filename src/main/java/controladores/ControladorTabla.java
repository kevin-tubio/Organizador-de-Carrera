package controladores;

import java.net.URL;
import java.util.ResourceBundle;

import dao.AccesadorAConfiguracion;
import dto.Configurable;
import dto.Tabla;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import entity.Materia;
import enumerados.Estado;
import enumerados.Periodo;
import enumerados.Tipo;
import enumerados.TipoConfiguracion;
import listado.Listado;
import util.Inyectable;

public class ControladorTabla implements Initializable, Inyectable {

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

	private ControladorPrincipal controlador;

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
		tabla.setPlaceholder(new Label(resourceBundle.getString("ListadoVacio")));
	}

	private void agregarSubscriptorAListado() {
		MapChangeListener<Integer, Materia> subscriptor = cambio -> tabla.getItems().setAll(cambio.getMap().values());
		Listado.obtenerListado().getListadoDeMaterias().addListener(subscriptor);
	}

	public void habilitarFunciones() {
		if (this.obtenerSeleccionado() != null) {
			this.controlador.habilitarFunciones();
		}
	}

	protected Materia obtenerSeleccionado() {
		return this.tabla.getSelectionModel().getSelectedItem();
	}

	public void borrarContextual() {
		this.controlador.borrarMateria(this.obtenerSeleccionado());
	}

	public void editarContextual() {
		this.controlador.editarMateria(this.obtenerSeleccionado());
		tabla.refresh();
	}

	public void agregarMateria() {
		this.controlador.agregarMateria();
	}

	public void guardarDimensionesTabla() {
		Tabla configuracion = new Tabla();
		guardarDimensiones(configuracion);
		guardarVisibles(configuracion);
		AccesadorAConfiguracion<Configurable> dao = new AccesadorAConfiguracion<>();
		dao.actualizarConfiguracion(configuracion.getConfig());
	}
	
	private void guardarDimensiones(Tabla configuracion) {
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

	private void guardarVisibles(Tabla configuracion) {
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
		AccesadorAConfiguracion<Configurable> dao = new AccesadorAConfiguracion<>();
		Tabla configuracion = new Tabla(dao.obtenerConfiguracion(new Configurable(TipoConfiguracion.TABLA)));
		if (configuracion.esValida()) {
			dimensionarColumnas(configuracion);
			recuperarVisibles(configuracion);
		}
	}

	private void dimensionarColumnas(Tabla config) {
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

	private void recuperarVisibles(Tabla configuracion) {
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

	@Override
	public void inyectarControlador(ControladorPrincipal controladorPrincipal) {
		this.controlador = controladorPrincipal;
	}
}
