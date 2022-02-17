package com.organizadorcarrera.sistema;

import com.organizadorcarrera.dao.AccesadorAMaterias;
import com.organizadorcarrera.entity.Materia;
import com.organizadorcarrera.excepciones.ArchivoException;
import com.organizadorcarrera.excepciones.ArchivoVacioException;
import com.organizadorcarrera.listado.Listado;
import com.organizadorcarrera.util.LangResource;

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
