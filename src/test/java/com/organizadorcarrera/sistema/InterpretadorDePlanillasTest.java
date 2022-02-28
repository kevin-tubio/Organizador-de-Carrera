package com.organizadorcarrera.sistema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.organizadorcarrera.entity.Course;
import com.organizadorcarrera.enumerados.CourseStatus;
import com.organizadorcarrera.enumerados.CoursePeriod;
import com.organizadorcarrera.exception.FileException;
import com.organizadorcarrera.exception.InvalidCourseException;
import com.organizadorcarrera.parser.ExcelFileParser;
import com.organizadorcarrera.parser.FileParser;
import com.organizadorcarrera.program.Program;

public class InterpretadorDePlanillasTest {

	private FileParser interpretador;

	@Before
	public void set() {
		interpretador = new ExcelFileParser();
	}

	@After
	public void reset() {
		Program.clearProgram();
	}

	@Test
	public void interpretarArchivoXls() {
		try {
			interpretador.generarListado("archivoDeEntrada/valido/plan_estudios.xls");
		} catch (FileException e) {
			fail();
		}
	}

	@Test
	public void interpretarArchivoXlsx() {
		try {
			interpretador.generarListado("archivoDeEntrada/valido/plan_estudios.xlsx");
		} catch (FileException e) {
			fail();
		}
	}

	@Test(expected = FileException.class)
	public void interpretarArchivoSinMaterias() throws FileException {
		interpretador.generarListado("archivoDeEntrada/invalido/sin_materias.xls");
	}

	@Test(expected = FileException.class)
	public void interpretarArchivoSinNumerosDeMateria() throws FileException {
		interpretador.generarListado("archivoDeEntrada/invalido/sin_numeros_de_materias.xls");
	}

	@Test(expected = FileException.class)
	public void interpretarArchivoAnioDeMateria() throws FileException {
		interpretador.generarListado("archivoDeEntrada/invalido/sin_anio_de_materias.xls");
	}

	@Test
	public void planillaSemiValida1() throws FileException {
		interpretador.generarListado("archivoDeEntrada/valido/prueba_1.xls");

		HashMap<Integer, Course> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(3,
				new Course(3, "Cuestiones de Sociología, Economía y Política", 2, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(592, new Course(592, "Introducción a la Problemática del Mundo Contemporáneo", 1,
				CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1265, new Course(1265, "Análisis Matemático I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1269, new Course(1269, "Física II", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1240,
				new Course(1240, "Algoritmos y Programación III", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1242, new Course(1242, "Matemáticas Especiales", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1270, new Course(1270, "Física III", 3, CoursePeriod.PRIMER_CUATRIMESTRE));

		var listado = Program.getInstance();

		assertEquals(7, listado.getCoursesCount());
		assertEquals(listadoDeMaterias, listado.getProgramMap());
	}

	@Test
	public void planillaValida1() throws FileException, InvalidCourseException {
		interpretador.generarListado("archivoDeEntrada/valido/prueba_1.xlsx");

		HashMap<Integer, Course> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(3,
				new Course(3, "Cuestiones de Sociología, Economía y Política", 2, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(592, new Course(592, "Introducción a la Problemática del Mundo Contemporáneo", 1,
				CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1265, new Course(1265, "Análisis Matemático I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1269, new Course(1269, "Física II", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1240,
				new Course(1240, "Algoritmos y Programación III", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1242, new Course(1242, "Matemáticas Especiales", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1270, new Course(1270, "Física III", 3, CoursePeriod.PRIMER_CUATRIMESTRE));

		var listado = Program.getInstance();

		assertEquals(7, listado.getCoursesCount());
		assertEquals(listadoDeMaterias, listado.getProgramMap());
		assertEquals(CourseStatus.NO_CURSADA, listado.getCourse(3).getCourseStatus());
		assertEquals(CourseStatus.APROBADA, listado.getCourse(592).getCourseStatus());
		assertEquals(CourseStatus.APROBADA, listado.getCourse(1265).getCourseStatus());
		assertEquals(CourseStatus.APROBADA, listado.getCourse(1269).getCourseStatus());
		assertEquals(CourseStatus.NO_CURSADA, listado.getCourse(1240).getCourseStatus());
		assertEquals(CourseStatus.REGULARIZADA, listado.getCourse(1242).getCourseStatus());
		assertEquals(CourseStatus.NO_CURSADA, listado.getCourse(1270).getCourseStatus());
		assertEquals("", listado.getCourse(3).getGrade());
		assertEquals("5", listado.getCourse(592).getGrade());
		assertEquals("6", listado.getCourse(1265).getGrade());
		assertEquals("8", listado.getCourse(1269).getGrade());
		assertEquals("", listado.getCourse(1240).getGrade());
		assertEquals("-", listado.getCourse(1242).getGrade());
		assertEquals("", listado.getCourse(1270).getGrade());
	}

	@Test
	public void planillaSemiValida2() throws FileException, InvalidCourseException {
		interpretador.generarListado("archivoDeEntrada/valido/prueba_2.xls");

		HashMap<Integer, Course> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(3,
				new Course(3, "Cuestiones de Sociología, Economía y Política", 2, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(15, new Course(15, "Análisis Matemático I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));

		var listado = Program.getInstance();

		assertEquals(2, listado.getCoursesCount());
		assertEquals(listadoDeMaterias, listado.getProgramMap());
		assertEquals(CourseStatus.NO_CURSADA, listado.getCourse(3).getCourseStatus());
		assertEquals("", listado.getCourse(3).getGrade());
	}

}
