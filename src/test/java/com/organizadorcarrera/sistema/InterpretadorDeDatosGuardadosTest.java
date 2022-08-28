package com.organizadorcarrera.sistema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.organizadorcarrera.service.TextFileParserService;
import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.enums.CourseStatus;
import com.organizadorcarrera.enums.CoursePeriod;
import com.organizadorcarrera.exception.FileException;
import com.organizadorcarrera.exception.InvalidCourseException;
import com.organizadorcarrera.program.Program;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InterpretadorDeDatosGuardadosTest {

	private final TextFileParserService textFileParserService;
	private final Program program;

	@Autowired
	public InterpretadorDeDatosGuardadosTest(TextFileParserService textFileParserService, Program program) {
		this.textFileParserService = textFileParserService;
		this.program = program;
	}

	@BeforeEach
	public void set() {
		program.clearProgram();
	}

	@AfterEach
	public void reset() {
		program.clearProgram();
	}

	@Test
	void interpretarArchivoTxtVacio() {
		assertThrows(FileException.class, () -> textFileParserService.generarListado("archivoDeEntrada/invalido/vacio.txt"));
	}

	@Test
	void interpretarArchivoTxt() throws FileException, InvalidCourseException {
		textFileParserService.generarListado("archivoDeEntrada/valido/materias_guardadas.txt");

		HashMap<Integer, Course> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(1269,
				new Course(1269, "Algoritmos y Programación III", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(592, new Course(592, "Introducción a la Problemática del Mundo Contemporáneo", 1,
				CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(862, new Course(862, "Álgebra I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1242, new Course(1242, "Matemáticas Especiales", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));

		assertEquals(4, program.getCoursesCount());
		assertEquals(listadoDeMaterias, program.getProgramMap());
		assertEquals(CourseStatus.APROBADA, program.getCourse(1269).getCourseStatus());
		assertEquals(CourseStatus.NO_CURSADA, program.getCourse(592).getCourseStatus());
		assertEquals(CourseStatus.APROBADA, program.getCourse(862).getCourseStatus());
		assertEquals(CourseStatus.REGULARIZADA, program.getCourse(1242).getCourseStatus());
		assertEquals("10", program.getCourse(1269).getGrade());
		assertEquals("", program.getCourse(592).getGrade());
		assertEquals("4", program.getCourse(862).getGrade());
		assertEquals("-", program.getCourse(1242).getGrade());
	}

	@Test
	void interpretarArchivoTxtCorrelativas() throws FileException, InvalidCourseException {
		textFileParserService.generarListado("archivoDeEntrada/valido/materias_guardadas_correlativas.txt");

		HashMap<Integer, Course> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(1269,
				new Course(1269, "Algoritmos y Programación III", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(592, new Course(592, "Introducción a la Problemática del Mundo Contemporáneo", 1,
				CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(862, new Course(862, "Álgebra I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1242, new Course(1242, "Matemáticas Especiales", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));

		assertEquals(4, program.getCoursesCount());
		assertEquals(listadoDeMaterias, program.getProgramMap());
		assertEquals(CourseStatus.APROBADA, program.getCourse(1269).getCourseStatus());
		assertEquals(CourseStatus.NO_CURSADA, program.getCourse(592).getCourseStatus());
		assertEquals(CourseStatus.APROBADA, program.getCourse(862).getCourseStatus());
		assertEquals(CourseStatus.REGULARIZADA, program.getCourse(1242).getCourseStatus());
		assertEquals("10", program.getCourse(1269).getGrade());
		assertEquals("", program.getCourse(592).getGrade());
		assertEquals("4", program.getCourse(862).getGrade());
		assertEquals("-", program.getCourse(1242).getGrade());

		Set<Course> aYP = new HashSet<>();
		aYP.add(new Course(1242, "Matemáticas Especiales", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		aYP.add(new Course(862, "Álgebra I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));

		Set<Course> algebra = new HashSet<>();
		algebra.add(new Course(592, "Introducción a la Problemática del Mundo Contemporáneo", 1, CoursePeriod.PRIMER_CUATRIMESTRE));

		assertEquals(aYP, program.getCourse(1269).getCorrelatives());
		assertEquals(algebra, program.getCourse(862).getCorrelatives());
	}

}
