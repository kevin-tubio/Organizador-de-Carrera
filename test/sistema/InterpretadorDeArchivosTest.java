package sistema;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class InterpretadorDeArchivosTest {

	@Test
	public void interpretaArchivoOpenOficce() {
		InterpretadorDeArchivos interpretador = new InterpretadorDeArchivos();
		
		try {
			
			interpretador.generarListado("archivoDeEntrada/plan_estudios.xlsx");
		} catch (IOException e) {
			fail();
		}
	}
}