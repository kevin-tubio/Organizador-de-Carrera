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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import enumerados.Estado;
import enumerados.Periodo;
import enumerados.Tipo;
import listado.Listado;
import listado.Materia;
import util.Inyectable;
import util.LangResource;

import net.synedra.validatorfx.Check;
import net.synedra.validatorfx.ValidationResult;
import net.synedra.validatorfx.Validator;

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
	@FXML
	private Label mensajeError;
	@FXML
	private BorderPane contenedor;

	private ObservableList<Materia> materias;
	private ObservableList<Materia> correlativas;
	private FilteredList<Materia> listaFiltrada;
	private Validator validador;
	private Materia materiaInyectada;
	private ControladorPrincipal controlador;

	public ControladorAgregarMateria() {
		this.validador = new Validator();
		this.materias = FXCollections.observableArrayList(Listado.obtenerListado().getListadoDeMaterias().values());
		this.correlativas = FXCollections.observableArrayList();
		this.listaFiltrada = new FilteredList<>(this.materias);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle resourceBundle) {
		this.inicializarCampos();
		this.crearValidadorDeCampos();
		this.agregarSubscriptorAlBuscador();
	}

	private void inicializarCampos() {
		this.inicializarChoiceBoxes();
		this.inicializarSpinners();
		this.inicializarBotones();
		this.inicializarListaDeCorrelativas();
		this.inicializarListaDeMaterias();
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

	private void inicializarBotones() {
		this.aceptar.disableProperty().bind(validador.containsErrorsProperty());
		this.cancelar.setCancelButton(true);
	}

	private void inicializarListaDeCorrelativas() {
		this.listadoCorrelativas.setItems(this.correlativas);
		this.listadoCorrelativas.setPlaceholder(new Label(LangResource.getString("ListaCorrelativasVacia")));
		this.itemContextualQuitar.disableProperty()
				.bind(listadoCorrelativas.getSelectionModel().selectedItemProperty().isNull());
	}

	private void inicializarListaDeMaterias() {
		this.listado.setVisible(false);
		this.listado.setPlaceholder(new Label(LangResource.getString("ListadoVacio")));
		this.listado.setItems(this.listaFiltrada);
		this.itemContextualAgregar.disableProperty().bind(listado.getSelectionModel().selectedItemProperty().isNull());
	}

	private void crearValidadorDeCampos() {
		var checkID = crearCheckId();
		var checkNombre = crearCheckNombre();
		this.id.textProperty().addListener((observable, viejo, nuevo) -> checkID.recheck());
		this.nombre.textProperty().addListener((observable, viejo, nuevo) -> checkNombre.recheck());
		this.validador.validationResultProperty()
				.addListener((observable, viejo, nuevo) -> this.mensajeError.setText(obtenerMensajeDeError(nuevo)));
	}

	private String obtenerMensajeDeError(ValidationResult nuevo) {
		var iterador = nuevo.getMessages().listIterator();
		return (iterador.hasNext()) ? iterador.next().getText() : "";
	}

	private Check crearCheckId() {
		return validador.createCheck().withMethod(in -> {
			if (campoVacio(this.id))
				in.error(LangResource.getString("InputIdVacio"));
			if (!this.id.getText().matches("^[0-9]+$"))
				in.error(LangResource.getString("InputIdInvalido"));
			if (idRepetido())
				in.error(LangResource.getString("InputIdRepetido"));
			if (materiaFormaCiclo())
				in.error(LangResource.getString("InputIdCiclo"));
		}).dependsOn("id", this.id.textProperty()).decorates(this.id);
	}

	private boolean idRepetido() {
		return !idCoincideConMateria(this.materiaInyectada)
				&& Listado.obtenerListado().contieneMateria(this.id.getText());
	}

	private boolean idCoincideConMateria(Materia materia) {
		return materia != null && this.id.getText().equals(String.valueOf(materia.getNumeroActividad()));
	}

	private boolean materiaFormaCiclo() {
		return idCoincideConMateria(this.materiaInyectada) && this.correlativas.contains(this.materiaInyectada);
	}

	private Check crearCheckNombre() {
		return validador.createCheck().withMethod(in -> {
			if (campoVacio(this.nombre))
				in.error(LangResource.getString("InputNombreVacio"));
		}).dependsOn("nombre", this.nombre.textProperty()).decorates(this.nombre);
	}

	private boolean campoVacio(TextField campo) {
		return campo.textProperty().getValue().isBlank();
	}

	private void agregarSubscriptorAlBuscador() {
		this.contenedor.setOnMousePressed(event -> this.contenedor.requestFocus());
		this.buscador.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (Boolean.TRUE.equals(newValue)) {
				this.listado.setVisible(true);
				aplicarFiltro(this.buscador.getText());
			} else if (!this.listado.isFocused()) {
				this.buscador.clear();
				this.listado.setVisible(false);
			}
		});
		this.buscador.textProperty().addListener((observable, viejo, nuevo) -> aplicarFiltro(nuevo));
		agregarSubscriptorAListaDeMaterias();
	}

	private void aplicarFiltro(String filtro) {
		this.listaFiltrada.setPredicate(materia -> noEsFiltrada(materia, filtro.toLowerCase()));
	}

	private boolean noEsFiltrada(Materia materia, String filtro) {
		var identificador = String.valueOf(materia.getNumeroActividad());
		var nombreMateria = materia.getNombre().toLowerCase();
		var resultado = true;
		if (!validador.containsErrors())
			resultado = !idCoincideConMateria(materia);
		return resultado && busquedaCoincide(nombreMateria, identificador, filtro);
	}

	private boolean busquedaCoincide(String nombreMateria, String identifiacador, String busqueda) {
		return nombreMateria.indexOf(busqueda) != -1 || identifiacador.indexOf(busqueda) != -1;
	}

	private void agregarSubscriptorAListaDeMaterias() {
		this.listado.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (Boolean.FALSE.equals(newValue)) {
				this.listado.setVisible(false);
				if (!this.buscador.isFocused())
					this.buscador.clear();
			}
		});
	}

//----------------------------------------------------------------------------------------------------------------------

	@Override
	public void inyectarControlador(ControladorPrincipal controladorPrincipal) {
		this.controlador = controladorPrincipal;
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
		this.materias.removeAll(this.correlativas);
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

	private void actualizarDatos(Materia materia) {
		materia.setNombre(nombre.getText());
		materia.setPeriodo(periodo.getValue());
		materia.setAnio(anio.getValue());
		materia.setEstado(estado.getValue());
		materia.setTipo(tipo.getValue());
		materia.setHorasSemanales(hs.getValue());
		materia.setCreditos(creditos.getValue());
		materia.setCorrelativas(new HashSet<>(this.correlativas));
		if (estado.getValue() == Estado.APROBADA)
			materia.setCalificacion(nota.getValue());
	}

	private void guardarYCerrar() {
		if (this.materiaInyectada == null)
			this.materiaInyectada = generarNuevaMateria();
		if (!idCoincideConMateria(this.materiaInyectada)) {
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
		if (validador.validate())
			guardarYCerrar();
	}

}
