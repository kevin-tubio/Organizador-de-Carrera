package com.organizadorcarrera.sistema;

import com.organizadorcarrera.excepciones.ArchivoException;
import com.organizadorcarrera.listado.Listado;

public interface InterpretadorDeArchivos {

	public Listado generarListado(String ruta) throws ArchivoException;

}
