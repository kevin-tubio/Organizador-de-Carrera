package com.organizadorcarrera.sistema;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
import com.organizadorcarrera.parser.TextFileParser;
import com.organizadorcarrera.program.Program;

public class InterpretadorDeDatosGuardadosTest {

	private FileParser interpretador;

	@Before
	public void set() {
		interpretador = new ExcelFileParser();
	}

	@After
	public void reset() {
		Program.clearProgram();
	}

	@Test(expected = FileException.class)
	public void interpretarArchivoTxtVacio() throws FileException {
		interpretador = new TextFileParser();
		interpretador.generarListado("archivoDeEntrada/invalido/vacio.txt");
	}

	@Test
	public void interpretarArchivoTxt() throws FileException, InvalidCourseException {
		interpretador = new TextFileParser();
		interpretador.generarListado("archivoDeEntrada/valido/materias_guardadas.txt");

		HashMap<Integer, Course> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(1269,
				new Course(1269, "Algoritmos y Programación III", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(592, new Course(592, "Introducción a la Problemática del Mundo Contemporáneo", 1,
				CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(862, new Course(862, "Álgebra I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1242, new Course(1242, "Matemáticas Especiales", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		var listado = Program.getInstance();

		assertEquals(4, listado.getCoursesCount());
		assertEquals(listadoDeMaterias, listado.getProgramMap());
		assertEquals(CourseStatus.APROBADA, listado.getCourse(1269).getCourseStatus());
		assertEquals(CourseStatus.NO_CURSADA, listado.getCourse(592).getCourseStatus());
		assertEquals(CourseStatus.APROBADA, listado.getCourse(862).getCourseStatus());
		assertEquals(CourseStatus.REGULARIZADA, listado.getCourse(1242).getCourseStatus());
		assertEquals("10", listado.getCourse(1269).getGrade());
		assertEquals("", listado.getCourse(592).getGrade());
		assertEquals("4", listado.getCourse(862).getGrade());
		assertEquals("-", listado.getCourse(1242).getGrade());
	}

	@Test
	public void interpretarArchivoTxtCorrelativas() throws FileException, InvalidCourseException {
		interpretador = new TextFileParser();
		interpretador.generarListado("archivoDeEntrada/valido/materias_guardadas_correlativas.txt");

		HashMap<Integer, Course> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(1269,
				new Course(1269, "Algoritmos y Programación III", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(592, new Course(592, "Introducción a la Problemática del Mundo Contemporáneo", 1,
				CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(862, new Course(862, "Álgebra I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1242, new Course(1242, "Matemáticas Especiales", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		var listado = Program.getInstance();

		assertEquals(4, listado.getCoursesCount());
		assertEquals(listadoDeMaterias, listado.getProgramMap());
		assertEquals(CourseStatus.APROBADA, listado.getCourse(1269).getCourseStatus());
		assertEquals(CourseStatus.NO_CURSADA, listado.getCourse(592).getCourseStatus());
		assertEquals(CourseStatus.APROBADA, listado.getCourse(862).getCourseStatus());
		assertEquals(CourseStatus.REGULARIZADA, listado.getCourse(1242).getCourseStatus());
		assertEquals("10", listado.getCourse(1269).getGrade());
		assertEquals("", listado.getCourse(592).getGrade());
		assertEquals("4", listado.getCourse(862).getGrade());
		assertEquals("-", listado.getCourse(1242).getGrade());

		Set<Course> aYP = new HashSet<>();
		aYP.add(new Course(1242, "Matemáticas Especiales", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		aYP.add(new Course(862, "Álgebra I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));

		Set<Course> algebra = new HashSet<>();
		algebra.add(new Course(592, "Introducción a la Problemática del Mundo Contemporáneo", 1,
				CoursePeriod.PRIMER_CUATRIMESTRE));

		assertEquals(aYP, listado.getCourse(1269).getCorrelatives());
		assertEquals(algebra, listado.getCourse(862).getCorrelatives());
	}

}
