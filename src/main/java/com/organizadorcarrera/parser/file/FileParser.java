package com.organizadorcarrera.parser.file;

import com.organizadorcarrera.exception.FileException;

public interface FileParser {

	void generarListado(String ruta) throws FileException;

}
