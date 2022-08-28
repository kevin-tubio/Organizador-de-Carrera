package com.organizadorcarrera.service;

import com.organizadorcarrera.exception.FileException;

public interface FileParserService {

	void generarListado(String ruta) throws FileException;

}
