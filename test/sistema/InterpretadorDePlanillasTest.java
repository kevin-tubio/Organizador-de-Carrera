package sistema;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import excepciones.ArchivoException;
import listado.Listado;

public class InterpretadorDePlanillasTest {

	private InterpretadorDePlanillas interpretador;

	@Before
	public void set() {
		interpretador = new InterpretadorDePlanillas();
	}

	@After
	public void reset() {
		Listado.borrarListado();
	}

	@Test
	public void interpretarArchivoXls() {
		try {
			interpretador.generarListado("archivoDeEntrada/valido/plan_estudios.xls");
		} catch (ArchivoException e) {
			fail();
		}
	}

	@Test
	public void interpretarArchivoXlsx() {
		try {
			interpretador.generarListado("archivoDeEntrada/valido/plan_estudios.xlsx");
		} catch (ArchivoException e) {
			fail();
		}
	}

	@Test(expected = ArchivoException.class)
	public void interpretarArchivoSinMaterias() throws ArchivoException {
		interpretador.generarListado("archivoDeEntrada/invalido/sin_materias.xls");
	}

	@Test(expected = ArchivoException.class)
	public void interpretarArchivoSinNumerosDeMateria() throws ArchivoException {
		interpretador.generarListado("archivoDeEntrada/invalido/sin_numeros_de_materias.xls");
	}

	@Test(expected = ArchivoException.class)
	public void interpretarArchivoAnioDeMateria() throws ArchivoException {
		interpretador.generarListado("archivoDeEntrada/invalido/sin_anio_de_materias.xls");
	}

}