package com.organizadorcarrera.sistema;

import com.organizadorcarrera.exception.ArchivoException;
import com.organizadorcarrera.listado.Listado;

public interface InterpretadorDeArchivos {

	public Listado generarListado(String ruta) throws ArchivoException;

}
