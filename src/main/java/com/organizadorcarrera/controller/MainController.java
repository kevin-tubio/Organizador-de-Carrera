package com.organizadorcarrera.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.organizadorcarrera.entity.Materia;
import com.organizadorcarrera.exception.ArchivoException;
import com.organizadorcarrera.listado.Listado;
import com.organizadorcarrera.service.ListadoService;
import com.organizadorcarrera.system.InterpretadorDeArchivos;
import com.organizadorcarrera.system.InterpretadorDeDatosGuardados;
import com.organizadorcarrera.system.InterpretadorDePlanillas;
import com.organizadorcarrera.util.DirectorVentana;

@Component
public class MainController implements Initializable {

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

	@Autowired
	private DirectorVentana directorVentana;

	@Autowired
	private ListadoService materiaService;

	private SimpleBooleanProperty cambiosSubject;
	private Logger logger;

	public MainController() {
		this.cambiosSubject = new SimpleBooleanProperty(false);
		this.logger = LoggerFactory.getLogger(MainController.class);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.itemGuardar.disableProperty().bind(this.cambiosSubject.not());
		this.deshabilitarFunciones();
		materiaService.recuperarListado();
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
		this.directorVentana.hacerVentanaAgregarMateria();
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
		this.directorVentana.hacerVentanaEditarMateria(materia);
	}

	public void persistirCambiosListado() {
		materiaService.persistirCambiosListado();
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
		this.directorVentana.hacerVentanaCierre();
	}

	void cerrarVentana() {
		Platform.exit();
	}
}
