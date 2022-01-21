package controladores;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import dao.AccesadorADatos;
import dao.AccesadorAMaterias;
import entity.Materia;
import excepciones.ArchivoException;
import listado.Listado;
import sistema.InterpretadorDeArchivos;
import sistema.InterpretadorDeDatosGuardados;
import sistema.InterpretadorDePlanillas;
import sistema.RecuperadorDatosGuardados;
import util.DirectorVentana;
import util.Inyectable;

public class ControladorPrincipal implements Initializable {

	@FXML
	private ControladorTabla planDeEstudiosController;

	@FXML
	private ControladorLista listaDeMateriasController;

	@FXML
	private MenuItem itemAgregar;

	@FXML
	private MenuItem itemBorrar;

	@FXML
	private MenuItem itemEditar;

	@FXML
	private TabPane tab;

	@FXML
	private MenuItem itemGuardar;

	@FXML
	private MenuItem itemSalir;

	@FXML
	private AnchorPane ancla;

	private SimpleBooleanProperty cambiosSubject;
	private Logger logger;

	public ControladorPrincipal() {
		this.cambiosSubject = new SimpleBooleanProperty(false);
		this.logger = LoggerFactory.getLogger(ControladorPrincipal.class);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.planDeEstudiosController.inyectarControlador(this);
		this.listaDeMateriasController.inyectarControlador(this);
		this.itemGuardar.disableProperty().bind(this.cambiosSubject.not());
		this.deshabilitarFunciones();
		var interpretador = new RecuperadorDatosGuardados();
		try {
			interpretador.generarListado("");
		} catch (ArchivoException e) {
			logger.debug(e.getMessage(), e);
		}
		planDeEstudiosController.recuperarDimensionesTabla();
	}

	public void abrirArchivo() {
		var fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"),
				new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		var archivo = fileChooser.showOpenDialog(new Stage());
		if (archivo != null) {
			Listado.borrarListado();
			Platform.runLater(() -> {
				try {
					cargarArchivo(archivo);
				} catch (ArchivoException e) {
					logger.debug(e.getMessage(), e);
				}
			});
			declararCambios();
		}
	}

	private void cargarArchivo(File archivo) throws ArchivoException {
		InterpretadorDeArchivos interpretador = null;
		if (archivo.getName().endsWith(".txt")) {
			interpretador = new InterpretadorDeDatosGuardados();
		} else {
			interpretador = new InterpretadorDePlanillas();
		}
		interpretador.generarListado(archivo.getAbsolutePath());
	}

	public void habilitarFunciones() {
		this.setFunciones(false);
	}

	public void deshabilitarFunciones() {
		this.setFunciones(true);
	}

	private void setFunciones(boolean valor) {
		this.itemEditar.setDisable(valor);
		this.itemBorrar.setDisable(valor);
	}

	public void borrarMateria() {
		ejecutarSegunSeleccionado(this::borrarMateria);
	}

	protected void borrarMateria(Materia seleccionada) {
		Listado.obtenerListado().borrarMateria(seleccionada);
		declararCambios();
	}

	public void agregarMateria() {
		Consumer<FXMLLoader> funcion = loader -> ((Inyectable) loader.getController()).inyectarControlador(this);
		new DirectorVentana("../fxml/AgregarMateria.fxml", "TituloVentanaAgregar", funcion).hacerVentanaModal();
	}

	public void editarMateria() {
		ejecutarSegunSeleccionado(this::editarMateria);
	}

	private void ejecutarSegunSeleccionado(Consumer<Materia> funcion) {
		switch (getTabLocalizedId()) {
		case "planDeEstudios":
			funcion.accept(planDeEstudiosController.obtenerSeleccionado());
			break;
		case "listaDeMaterias":
			funcion.accept(listaDeMateriasController.obtenerSeleccionado());
			break;
		default:
		}
		if (Listado.obtenerListado().getListadoDeMaterias().isEmpty()) {
			this.deshabilitarFunciones();
		}
	}

	private String getTabLocalizedId() {
		return this.tab.getSelectionModel().getSelectedItem().getContent().getId();
	}

	protected void editarMateria(Materia materia) {
		Consumer<FXMLLoader> funcion = loader -> {
			ControladorAgregarMateria controlador = loader.getController();
			controlador.inyectarControlador(this);
			controlador.inyectarMateria(materia);
		};
		new DirectorVentana("../fxml/AgregarMateria.fxml", "TituloVentanaEditar", funcion).hacerVentanaModal();
	}

	public void persistirCambiosListado() {
		Runnable runnable = () -> {
			var dao = new AccesadorAMaterias();
			synchronized (AccesadorADatos.class) {
				dao.borrarTodo();
				dao.persistirTodo(Listado.obtenerListado().getListadoDeMaterias().values());
			}
		};
		var saving = new Thread(runnable, "persistirCambiosListado");
		saving.start();
		this.cambiosSubject.set(false);
	}

	public void declararCambios() {
		this.cambiosSubject.set(true);
	}

	public void cerrarPrograma(Event cierre) {
		cierre.consume();
		this.planDeEstudiosController.guardarDimensionesTabla();
		if (!hayCambiosSinGuardar())
			cerrarVentana();
		else
			confirmarCierre();
	}

	private boolean hayCambiosSinGuardar() {
		return this.cambiosSubject.get();
	}

	private void confirmarCierre() {
		Consumer<FXMLLoader> funcion = loader -> ((Inyectable) loader.getController()).inyectarControlador(this);
		new DirectorVentana("../fxml/ConfirmacionCierre.fxml", "TituloVentanaConfirmarCierre", funcion)
				.hacerVentanaModal();
	}

	void cerrarVentana() {
		Platform.exit();
	}
}
