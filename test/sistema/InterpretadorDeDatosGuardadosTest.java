package sistema;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import excepciones.ArchivoException;
import excepciones.MateriaInvalidaException;
import listado.Listado;
import listado.Materia;
import listado.Materia.Estado;

public class InterpretadorDeDatosGuardadosTest {

	private InterpretadorDeArchivos interpretador;

	@Before
	public void set() {
		interpretador = new InterpretadorDePlanillas();
	}

	@After
	public void reset() {
		Listado.borrarListado();
	}

	@Test(expected = ArchivoException.class)
	public void interpretarArchivoTxtVacio() throws ArchivoException {
		interpretador = new InterpretadorDeDatosGuardados();
		interpretador.generarListado("archivoDeEntrada/invalido/vacio.txt");
	}

	@Test
	public void interpretarArchivoTxt() throws ArchivoException, MateriaInvalidaException {
		interpretador = new InterpretadorDeDatosGuardados();
		interpretador.generarListado("archivoDeEntrada/valido/materias_guardadas.txt");

		HashMap<Integer, Materia> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(1269, new Materia(1269, "Algoritmos y Programación III", 2, "2"));
		listadoDeMaterias.put(592, new Materia(592, "Introducción a la Problemática del Mundo Contemporáneo", 1, "1"));
		listadoDeMaterias.put(862, new Materia(862, "Álgebra I", 1, "1"));
		listadoDeMaterias.put(1242, new Materia(1242, "Matemáticas Especiales", 2, "2"));
		var listado = Listado.obtenerListado();

		assertEquals(4, listado.consultarCantidadDeMaterias());
		assertEquals(listadoDeMaterias, listado.getListadoDeMaterias());
		assertEquals(Estado.APROBADA, listado.obtenerMateria(1269).getEstado());
		assertEquals(Estado.NO_CURSADA, listado.obtenerMateria(592).getEstado());
		assertEquals(Estado.APROBADA, listado.obtenerMateria(862).getEstado());
		assertEquals(Estado.REGULARIZADA, listado.obtenerMateria(1242).getEstado());
		assertEquals(10, listado.obtenerMateria(1269).getCalificacion());
		assertEquals(0, listado.obtenerMateria(592).getCalificacion());
		assertEquals(4, listado.obtenerMateria(862).getCalificacion());
		assertEquals(0, listado.obtenerMateria(1242).getCalificacion());
	}

	@Test
	public void interpretarArchivoTxtCorrelativas() throws ArchivoException, MateriaInvalidaException {
		interpretador = new InterpretadorDeDatosGuardados();
		interpretador.generarListado("archivoDeEntrada/valido/materias_guardadas_correlativas.txt");

		HashMap<Integer, Materia> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(1269, new Materia(1269, "Algoritmos y Programación III", 2, "2"));
		listadoDeMaterias.put(592, new Materia(592, "Introducción a la Problemática del Mundo Contemporáneo", 1, "1"));
		listadoDeMaterias.put(862, new Materia(862, "Álgebra I", 1, "1"));
		listadoDeMaterias.put(1242, new Materia(1242, "Matemáticas Especiales", 2, "2"));
		var listado = Listado.obtenerListado();

		assertEquals(4, listado.consultarCantidadDeMaterias());
		assertEquals(listadoDeMaterias, listado.getListadoDeMaterias());
		assertEquals(Estado.APROBADA, listado.obtenerMateria(1269).getEstado());
		assertEquals(Estado.NO_CURSADA, listado.obtenerMateria(592).getEstado());
		assertEquals(Estado.APROBADA, listado.obtenerMateria(862).getEstado());
		assertEquals(Estado.REGULARIZADA, listado.obtenerMateria(1242).getEstado());
		assertEquals(10, listado.obtenerMateria(1269).getCalificacion());
		assertEquals(0, listado.obtenerMateria(592).getCalificacion());
		assertEquals(4, listado.obtenerMateria(862).getCalificacion());
		assertEquals(0, listado.obtenerMateria(1242).getCalificacion());

		Set<Materia> aYP = new HashSet<>();
		aYP.add(new Materia(1242, "Matemáticas Especiales", 2, "2"));
		aYP.add(new Materia(862, "Álgebra I", 1, "1"));

		Set<Materia> algebra = new HashSet<>();
		algebra.add(new Materia(592, "Introducción a la Problemática del Mundo Contemporáneo", 1, "1"));

		assertEquals(aYP, listado.obtenerMateria(1269).getCorrelativas());
		assertEquals(algebra, listado.obtenerMateria(862).getCorrelativas());
	}

}
