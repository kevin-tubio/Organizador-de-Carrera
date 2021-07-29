package sistema;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import excepciones.PlanillaInvalidaException;
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
		} catch (IOException | PlanillaInvalidaException e) {
			fail();
		}
	}

	@Test
	public void interpretarArchivoXlsx() {
		try {
			interpretador.generarListado("archivoDeEntrada/valido/plan_estudios.xlsx");
		} catch (IOException | PlanillaInvalidaException e) {
			fail();
		}
	}

	@Test(expected = PlanillaInvalidaException.class)
	public void interpretarArchivoSinMaterias() throws PlanillaInvalidaException {
		try {
			interpretador.generarListado("archivoDeEntrada/invalido/sin_materias.xls");
		} catch (IOException e) {
			fail();
		}
	}

	@Test(expected = PlanillaInvalidaException.class)
	public void interpretarArchivoSinNumerosDeMateria() throws PlanillaInvalidaException {
		try {
			interpretador.generarListado("archivoDeEntrada/invalido/sin_numeros_de_materias.xls");
		} catch (IOException e) {
			fail();
		}
	}

	@Test(expected = PlanillaInvalidaException.class)
	public void interpretarArchivoAnioDeMateria() throws PlanillaInvalidaException {
		try {
			interpretador.generarListado("archivoDeEntrada/invalido/sin_anio_de_materias.xls");
		} catch (IOException e) {
			fail();
		}
	}

}