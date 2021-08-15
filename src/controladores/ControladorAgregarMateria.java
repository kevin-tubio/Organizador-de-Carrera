package controladores;

import java.net.URL;
import java.util.ResourceBundle;

import enumerados.Estado;
import enumerados.Periodo;
import enumerados.Tipo;
import excepciones.MateriaInvalidaException;
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
import listado.Listado;
import listado.Materia;

public class ControladorAgregarMateria implements Initializable {

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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.inicializarChoiceBoxes();
		this.inicializarSpinners();
		this.inicializarListaDeCorrelativas();
		this.inicializarListaDeMaterias();
		this.inicializarBotones();
		this.agregarSubscriptorAlBuscador();
		this.itemContextualAgregar.disableProperty().bind(listado.getSelectionModel().selectedItemProperty().isNull());
		this.itemContextualQuitar.disableProperty()
				.bind(listadoCorrelativas.getSelectionModel().selectedItemProperty().isNull());
	}

	private void agregarSubscriptorAlBuscador() {
		this.buscador.textProperty().addListener((observable, viejo, nuevo) -> {
			this.listado.setVisible(true);
			if (nuevo.isEmpty())
				this.listado.setVisible(false);
			this.listaFiltrada.setPredicate(materia -> {
				String busqueda = nuevo.toLowerCase();
				return materia.getNombre().toLowerCase().indexOf(busqueda) != -1
						|| String.valueOf(materia.getNumeroActividad()).indexOf(busqueda) != -1;
			});
		});
	}

	private void inicializarBotones() {
		this.aceptar.disableProperty().bind(this.id.textProperty().isEmpty().or(this.nombre.textProperty().isEmpty()));
		this.cancelar.setCancelButton(true);
	}

	private void inicializarListaDeMaterias() {
		this.listado.setVisible(false);
		this.listado.setPlaceholder(new Label("No tiene materias en el listado"));
		this.materias = FXCollections.observableArrayList(Listado.obtenerListado().getListadoDeMaterias().values());
		this.listaFiltrada = new FilteredList<>(this.materias);
		this.listado.setItems(this.listaFiltrada);
	}

	private void inicializarListaDeCorrelativas() {
		this.correlativas = FXCollections.observableArrayList();
		this.listadoCorrelativas.setItems(this.correlativas);
		this.listadoCorrelativas.setPlaceholder(new Label("No tiene materias Correlativas"));
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

	public void cancelar() {
		((Stage) this.cancelar.getScene().getWindow()).close();
	}

	public void aceptar() {
		try {
			guardarYCerrar(generarNuevaMateria());
		} catch (MateriaInvalidaException | NumberFormatException e) {
			System.err.println(e.getMessage());
		}
	}

	private Materia generarNuevaMateria() throws MateriaInvalidaException {
		var nuevoId = Integer.parseInt(this.id.getText());
		var nuevoNombre = this.nombre.getText();

		var materia = new Materia(nuevoId, nuevoNombre, anio.getValue(), periodo.getValue(), estado.getValue(),
				nota.getValue());
		materia.setTipo(tipo.getValue());
		materia.setHorasSemanales(hs.getValue());
		materia.setCreditos(creditos.getValue());
		for (Materia correlativa : this.correlativas) {
			materia.setCorrelativa(correlativa);
		}

		return materia;
	}

	protected void inyectarMateria(Materia materia) {
		this.id.setText(String.valueOf(materia.getNumeroActividad()));
		this.nombre.setText(materia.getNombre());
		this.periodo.setValue(materia.getPeriodo());
		this.estado.setValue(materia.getEstado());
		this.anio.getValueFactory().setValue(materia.getAnio());
		if (materia.getEstado() == Estado.APROBADA) {
			this.nota.getValueFactory().setValue(Integer.parseInt(materia.getCalificacion()));
		}
		this.correlativas.addAll(materia.getCorrelativas());
		this.materias.removeAll(this.correlativas);
		this.aceptar.setOnAction(event -> cancelar());
	}

	private void guardarYCerrar(Materia materia) {
		Listado.obtenerListado().agregarMateria(materia);
		((Stage) this.aceptar.getScene().getWindow()).close();
	}

}
