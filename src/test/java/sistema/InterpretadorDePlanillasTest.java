package sistema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entity.Materia;
import enumerados.Estado;
import enumerados.Periodo;
import excepciones.ArchivoException;
import excepciones.MateriaInvalidaException;
import listado.Listado;

public class InterpretadorDePlanillasTest {

	private InterpretadorDeArchivos interpretador;

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

	@Test
	public void planillaSemiValida1() throws ArchivoException {
		interpretador.generarListado("archivoDeEntrada/valido/prueba_1.xls");

		HashMap<Integer, Materia> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(3,
				new Materia(3, "Cuestiones de Sociología, Economía y Política", 2, Periodo.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(592, new Materia(592, "Introducción a la Problemática del Mundo Contemporáneo", 1,
				Periodo.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1265, new Materia(1265, "Análisis Matemático I", 1, Periodo.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1269, new Materia(1269, "Física II", 2, Periodo.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1240,
				new Materia(1240, "Algoritmos y Programación III", 2, Periodo.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1242, new Materia(1242, "Matemáticas Especiales", 2, Periodo.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1270, new Materia(1270, "Física III", 3, Periodo.PRIMER_CUATRIMESTRE));

		var listado = Listado.obtenerListado();

		assertEquals(7, listado.consultarCantidadDeMaterias());
		assertEquals(listadoDeMaterias, listado.getListadoDeMaterias());
	}

	@Test
	public void planillaValida1() throws ArchivoException, MateriaInvalidaException {
		interpretador.generarListado("archivoDeEntrada/valido/prueba_1.xlsx");

		HashMap<Integer, Materia> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(3,
				new Materia(3, "Cuestiones de Sociología, Economía y Política", 2, Periodo.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(592, new Materia(592, "Introducción a la Problemática del Mundo Contemporáneo", 1,
				Periodo.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1265, new Materia(1265, "Análisis Matemático I", 1, Periodo.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1269, new Materia(1269, "Física II", 2, Periodo.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1240,
				new Materia(1240, "Algoritmos y Programación III", 2, Periodo.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1242, new Materia(1242, "Matemáticas Especiales", 2, Periodo.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1270, new Materia(1270, "Física III", 3, Periodo.PRIMER_CUATRIMESTRE));

		var listado = Listado.obtenerListado();

		assertEquals(7, listado.consultarCantidadDeMaterias());
		assertEquals(listadoDeMaterias, listado.getListadoDeMaterias());
		assertEquals(Estado.NO_CURSADA, listado.obtenerMateria(3).getEstado());
		assertEquals(Estado.APROBADA, listado.obtenerMateria(592).getEstado());
		assertEquals(Estado.APROBADA, listado.obtenerMateria(1265).getEstado());
		assertEquals(Estado.APROBADA, listado.obtenerMateria(1269).getEstado());
		assertEquals(Estado.NO_CURSADA, listado.obtenerMateria(1240).getEstado());
		assertEquals(Estado.REGULARIZADA, listado.obtenerMateria(1242).getEstado());
		assertEquals(Estado.NO_CURSADA, listado.obtenerMateria(1270).getEstado());
		assertEquals("", listado.obtenerMateria(3).getCalificacion());
		assertEquals("5", listado.obtenerMateria(592).getCalificacion());
		assertEquals("6", listado.obtenerMateria(1265).getCalificacion());
		assertEquals("8", listado.obtenerMateria(1269).getCalificacion());
		assertEquals("", listado.obtenerMateria(1240).getCalificacion());
		assertEquals("-", listado.obtenerMateria(1242).getCalificacion());
		assertEquals("", listado.obtenerMateria(1270).getCalificacion());
	}

	@Test
	public void planillaSemiValida2() throws ArchivoException, MateriaInvalidaException {
		interpretador.generarListado("archivoDeEntrada/valido/prueba_2.xls");

		HashMap<Integer, Materia> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(3,
				new Materia(3, "Cuestiones de Sociología, Economía y Política", 2, Periodo.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(15, new Materia(15, "Análisis Matemático I", 1, Periodo.PRIMER_CUATRIMESTRE));

		var listado = Listado.obtenerListado();

		assertEquals(2, listado.consultarCantidadDeMaterias());
		assertEquals(listadoDeMaterias, listado.getListadoDeMaterias());
		assertEquals(Estado.NO_CURSADA, listado.obtenerMateria(3).getEstado());
		assertEquals("", listado.obtenerMateria(3).getCalificacion());
	}

}
