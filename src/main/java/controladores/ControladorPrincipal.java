package controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import dao.AccesadorAMaterias;
import excepciones.ArchivoException;
import listado.Listado;
import listado.Materia;
import sistema.InterpretadorDeArchivos;
import sistema.InterpretadorDeDatosGuardados;
import sistema.InterpretadorDePlanillas;
import sistema.RecuperadorDatosGuardados;

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

	private SimpleBooleanProperty cambios;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.planDeEstudiosController.inyectarControlador(this);
		this.listaDeMateriasController.inyectarControlador(this);
		this.cambios = new SimpleBooleanProperty(false);
		this.itemGuardar.disableProperty().bind(this.cambios.not());
		this.deshabilitarFunciones();
		var interpretador = new RecuperadorDatosGuardados();
		try {
			interpretador.generarListado("");
		} catch (ArchivoException e) {
			System.out.println(e.getMessage());
		}
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
					System.out.println("intenta con otro archivo");
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
		switch (this.tab.getSelectionModel().getSelectedItem().getText()) {
		case "Plan de estudios":
			borrarMateria(planDeEstudiosController.obtenerSeleccionado());
			break;
		case "Lista de materias":
			borrarMateria(listaDeMateriasController.obtenerSeleccionado());
			break;
		default:
		}
		if (Listado.obtenerListado().getListadoDeMaterias().isEmpty()) {
			this.deshabilitarFunciones();
		}
	}

	protected void borrarMateria(Materia seleccionada) {
		Listado.obtenerListado().borrarMateria(seleccionada);
		declararCambios();
	}

	public void agregarMateria() throws IOException {
		var stage = new Stage();
		var loader = new FXMLLoader(this.getClass().getResource("../fxml/AgregarMateria.fxml"));
		Parent root = loader.load();
		((ControladorAgregarMateria) loader.getController()).inyectarControlador(this);
		var scene = new Scene(root);
		stage.setTitle("Agregar Materia");
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void editarMateria() throws IOException {
		switch (this.tab.getSelectionModel().getSelectedItem().getText()) {
		case "Plan de estudios":
			editarMateria(planDeEstudiosController.obtenerSeleccionado());
			break;
		case "Lista de materias":
			editarMateria(listaDeMateriasController.obtenerSeleccionado());
			break;
		default:
		}
		if (Listado.obtenerListado().getListadoDeMaterias().isEmpty()) {
			this.deshabilitarFunciones();
		}
	}

	protected void editarMateria(Materia materia) throws IOException {
		var stage = new Stage();
		var loader = new FXMLLoader(this.getClass().getResource("../fxml/AgregarMateria.fxml"));
		Parent root = loader.load();
		ControladorAgregarMateria controlador = loader.getController();
		controlador.inyectarMateria(materia);
		controlador.inyectarControlador(this);
		var scene = new Scene(root);
		stage.setTitle("Editar Materia");
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void persistirCambiosListado() {
		var hilo = new Thread(() -> {
			var dao = new AccesadorAMaterias();
			dao.borrarTodo();
			for (Materia actual : Listado.obtenerListado().getListadoDeMaterias().values()) {
				dao.actualizar(actual);
			}
		});
		hilo.start();
		this.cambios.set(false);
	}

	public void declararCambios() {
		this.cambios.set(true);
	}

}
