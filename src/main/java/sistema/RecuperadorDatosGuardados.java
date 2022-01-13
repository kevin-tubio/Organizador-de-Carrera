package sistema;

import dao.AccesadorAMaterias;
import entity.Materia;
import excepciones.ArchivoException;
import excepciones.ArchivoVacioException;
import listado.Listado;
import util.LangResource;

public class RecuperadorDatosGuardados implements InterpretadorDeArchivos {

	@Override
	public Listado generarListado(String ruta) throws ArchivoException {
		var listado = Listado.obtenerListado();
		var dao = new AccesadorAMaterias();
		var lista = dao.obtenerTodos();

		if (lista.isEmpty()) {
			throw new ArchivoVacioException(LangResource.getString("BaseDatosVacia"));
		}
		for (Materia materia : lista) {
			listado.agregarMateria(materia);
		}

		return listado;
	}
}
