package controladores;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import excepciones.ArchivoException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import listado.Listado;
import sistema.InterpretadorDeArchivos;
import sistema.InterpretadorDeDatosGuardados;
import sistema.InterpretadorDePlanillas;

public class ControladorPrincipal implements Initializable {

	@FXML
	private ControladorTabla planDeEstudiosController;
	@FXML
	private ControladorLista listaDeMateriasController;
	@FXML
	private MenuItem itemAgregar;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.planDeEstudiosController.inyectar(this);
		this.listaDeMateriasController.inyectar(this);
	}

	public void abrirArchivo() {
		var fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"),
				new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		try {
			var archivo = fileChooser.showOpenDialog(new Stage());
			Listado.borrarListado();
			cargarArchivo(archivo);
		} catch (ArchivoException e) {
			System.out.println("intenta con otro archivo");
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

	public void agregarMateria() throws IOException {
		var stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("../fxml/AgregarMateria.fxml"));
		var scene = new Scene(root);
		stage.setTitle("Agregar Materia");
		stage.setScene(scene);
		stage.showAndWait();
	}

}
