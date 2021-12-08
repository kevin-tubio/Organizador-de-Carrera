package sistema;

import dao.AccesadorAMaterias;
import excepciones.ArchivoException;
import excepciones.ArchivoVacioException;
import listado.Listado;
import listado.Materia;

public class RecuperadorDatosGuardados implements InterpretadorDeArchivos {

	@Override
	public Listado generarListado(String ruta) throws ArchivoException {
		var listado = Listado.obtenerListado();
		var dao = new AccesadorAMaterias();
		var lista = dao.obtenerTodos();

		if (lista.isEmpty()) {
			throw new ArchivoVacioException("No se encontraron materias en la base de datos");
		}
		for (Materia materia : lista) {
			listado.agregarMateria(materia);
		}

		return listado;
	}
}
