package sistema;

import excepciones.ArchivoException;
import listado.Listado;

public interface InterpretadorDeArchivos {

	public Listado generarListado(String ruta) throws ArchivoException;

}
