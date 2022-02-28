package com.organizadorcarrera.parser;

import com.organizadorcarrera.exception.FileException;
import com.organizadorcarrera.program.Program;

public interface FileParser {

	public Program generarListado(String ruta) throws FileException;

}
