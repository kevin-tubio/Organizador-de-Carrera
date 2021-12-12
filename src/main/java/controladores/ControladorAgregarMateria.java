package controladores;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import enumerados.Estado;
import enumerados.Periodo;
import enumerados.Tipo;
import listado.Listado;
import listado.Materia;

import net.synedra.validatorfx.Validator;
import util.Inyectable;

public class ControladorAgregarMateria implements Initializable, Inyectable {

	@FXML
	private Button aceptar;
	@FXML
	private Button cancelar;
	@FXML
	private TextField id;
	@FXML
	private TextField nombre;
	@FXML
	private TextField buscador;
	@FXML
	private ChoiceBox<Tipo> tipo;
	@FXML
	private ChoiceBox<Periodo> periodo;
	@FXML
	private ChoiceBox<Estado> estado;
	@FXML
	private Spinner<Integer> nota;
	@FXML
	private Spinner<Integer> anio;
	@FXML
	private Spinner<Integer> hs;
	@FXML
	private Spinner<Double> creditos;
	@FXML
	private ListView<Materia> listado;
	@FXML
	private ListView<Materia> listadoCorrelativas;
	@FXML
	private MenuItem itemContextualAgregar;
	@FXML
	private MenuItem itemContextualQuitar;

	private ObservableList<Materia> materias;
	private ObservableList<Materia> correlativas;
	private FilteredList<Materia> listaFiltrada;
	private Validator validador;
	private Materia materiaInyectada;
	private ControladorPrincipal controlador;

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.inicializarCampos();
		this.inicializarListaDeCorrelativas(resourceBundle);
		this.inicializarListaDeMaterias(resourceBundle);
		this.agregarSubscriptorAlBuscador();
		this.itemContextualAgregar.disableProperty().bind(listado.getSelectionModel().selectedItemProperty().isNull());
		this.itemContextualQuitar.disableProperty()
				.bind(listadoCorrelativas.getSelectionModel().selectedItemProperty().isNull());
	}

	private void inicializarCampos() {
		this.crearValidadorDeCampos();
		this.inicializarChoiceBoxes();
		this.inicializarSpinners();
		this.inicializarBotones();
	}

	private void agregarSubscriptorAlBuscador() {
		this.buscador.textProperty().addListener((observable, viejo, nuevo) -> {
			this.listado.setVisible(true);
			if (nuevo.isEmpty())
				this.listado.setVisible(false);
			this.listaFiltrada.setPredicate(materia -> busquedaCoincide(materia, nuevo.toLowerCase()));
		});
	}

	private boolean busquedaCoincide(Materia materia, String busqueda) {
		var nombreMateria = materia.getNombre().toLowerCase();
		var idMateria = String.valueOf(materia.getNumeroActividad());
		return nombreMateria.indexOf(busqueda) != -1 || idMateria.indexOf(busqueda) != -1;
	}

	@Override
	public void inyectarControlador(ControladorPrincipal controladorPrincipal) {
		this.controlador = controladorPrincipal;
	}

	private void crearValidadorDeCampos() {
		this.validador = new Validator();
		var checkID = validador.createCheck().withMethod(in -> {
			if (campoVacio(this.id))
				in.error(resourceBundle.getString("InputIdVacio"));
			if (!this.id.getText().matches("^[0-9]+$"))
				in.error(resourceBundle.getString("InputIdInvalido"));
		}).dependsOn("id", this.id.textProperty()).decorates(this.id);

		var checkNombre = validador.createCheck().withMethod(in -> {
			if (campoVacio(this.nombre))
				in.error(resourceBundle.getString("InputNombreVacio"));
		}).dependsOn("nombre", this.nombre.textProperty()).decorates(this.nombre);

		this.id.textProperty().addListener((observable, viejo, nuevo) -> checkID.recheck());
		this.nombre.textProperty().addListener((observable, viejo, nuevo) -> checkNombre.recheck());
	}

	private boolean campoVacio(TextField campo) {
		return campo.textProperty().getValue().isBlank();
	}

	private void inicializarBotones() {
		this.aceptar.disableProperty().bind(validador.containsErrorsProperty());
		this.cancelar.setCancelButton(true);
	}

	private void inicializarListaDeMaterias(ResourceBundle resourceBundle) {
		this.listado.setVisible(false);
		this.listado.setPlaceholder(new Label(resourceBundle.getString("ListadoVacio")));
		this.materias = FXCollections.observableArrayList(Listado.obtenerListado().getListadoDeMaterias().values());
		this.listaFiltrada = new FilteredList<>(this.materias);
		this.listado.setItems(this.listaFiltrada);
	}

	private void inicializarListaDeCorrelativas(ResourceBundle resourceBundle) {
		this.correlativas = FXCollections.observableArrayList();
		this.listadoCorrelativas.setItems(this.correlativas);
		this.listadoCorrelativas.setPlaceholder(new Label(resourceBundle.getString("ListaCorrelativasVacia")));
	}

	private void inicializarChoiceBoxes() {
		this.tipo.getItems().addAll(Tipo.values());
		this.tipo.setValue(Tipo.MATERIA);
		this.periodo.getItems().addAll(Periodo.values());
		this.periodo.setValue(Periodo.PRIMER_CUATRIMESTRE);
		this.estado.getItems().addAll(Estado.values());
		this.estado.setValue(Estado.NO_CURSADA);
	}

	private void inicializarSpinners() {
		this.nota.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(4, 10));
		this.nota.disableProperty()
				.bind(estado.getSelectionModel().selectedItemProperty().isNotEqualTo(Estado.APROBADA));
		this.anio.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
		this.anio.getValueFactory().setValue(1);
		this.hs.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20));
		this.hs.getValueFactory().setValue(4);
		this.creditos.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 20.0));
		this.creditos.getValueFactory().setValue(0.0);
	}

	public void agregarCorrelativa() {
		Materia materia = this.listado.getSelectionModel().getSelectedItem();
		this.correlativas.add(materia);
		this.materias.remove(materia);
	}

	public void quitarCorrelativa() {
		Materia materia = this.listadoCorrelativas.getSelectionModel().getSelectedItem();
		this.correlativas.remove(materia);
		this.materias.add(materia);
	}

	private Materia generarNuevaMateria() {
		var nuevoId = Integer.parseInt(this.id.getText());
		var nuevoNombre = this.nombre.getText();
		return new Materia(nuevoId, nuevoNombre);
	}

	protected void inyectarMateria(Materia materia) {
		this.materiaInyectada = materia;
		this.id.setText(String.valueOf(materia.getNumeroActividad()));
		this.nombre.setText(materia.getNombre());
		this.periodo.setValue(materia.getPeriodo());
		this.estado.setValue(materia.getEstado());
		this.anio.getValueFactory().setValue(materia.getAnio());
		if (materia.getEstado() == Estado.APROBADA)
			this.nota.getValueFactory().setValue(Integer.parseInt(materia.getCalificacion()));
		this.hs.getValueFactory().setValue(materia.getHorasSemanales());
		this.tipo.setValue(materia.getTipo());
		this.correlativas.addAll(materia.getCorrelativas());
		this.creditos.getValueFactory().setValue(materia.getCreditos());
		this.materias.remove(materia);
		this.materias.removeAll(this.correlativas);
	}

	private void actualizarDatos(Materia materia) {
		materia.setNombre(nombre.getText());
		materia.setPeriodo(periodo.getValue());
		materia.setAnio(anio.getValue());
		if (estado.getValue() == Estado.APROBADA) {
			materia.setCalificacion(nota.getValue());
		}
		materia.setEstado(estado.getValue());
		materia.setTipo(tipo.getValue());
		materia.setHorasSemanales(hs.getValue());
		materia.setCreditos(creditos.getValue());
		materia.setCorrelativas(new HashSet<>(this.correlativas));
	}

	private void guardarYCerrar() {
		if (this.materiaInyectada == null)
			this.materiaInyectada = generarNuevaMateria();
		if (materiaInyectada.getNumeroActividad() != Integer.parseInt(this.id.getText())) {
			var nuevaMateria = generarNuevaMateria();
			actualizarDatos(nuevaMateria);
			Listado.obtenerListado().reemplazarMateria(materiaInyectada, nuevaMateria);
		} else {
			actualizarDatos(materiaInyectada);
			Listado.obtenerListado().agregarMateria(materiaInyectada);
		}
		this.controlador.declararCambios();
		cerrar();
	}

	public void cerrar() {
		((Stage) this.cancelar.getScene().getWindow()).close();
	}

	public void aceptar() {
		if (validador.validate()) {
			guardarYCerrar();
		}
	}

}
