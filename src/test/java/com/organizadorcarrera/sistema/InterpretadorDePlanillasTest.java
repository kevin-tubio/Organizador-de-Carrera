package com.organizadorcarrera.sistema;

import java.util.HashMap;

import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.enums.CourseStatus;
import com.organizadorcarrera.enums.CoursePeriod;
import com.organizadorcarrera.exception.FileException;
import com.organizadorcarrera.exception.InvalidCourseException;
import com.organizadorcarrera.repository.ProgramRepository;
import com.organizadorcarrera.parser.file.ExcelFileParser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InterpretadorDePlanillasTest {

	private final ExcelFileParser excelFileParserService;
	private final ProgramRepository programRepository;

	@Autowired
	public InterpretadorDePlanillasTest(ExcelFileParser excelFileParserService, ProgramRepository programService) {
		this.excelFileParserService = excelFileParserService;
		this.programRepository = programService;
	}

	@BeforeEach
	public void set() {
		programRepository.deleteAll();
	}

	@AfterEach
	public void reset() {
		programRepository.deleteAll();
	}

	@Test
	void interpretarArchivoXls() {
		try {
			excelFileParserService.generarListado("archivoDeEntrada/valido/plan_estudios.xls");
		} catch (FileException e) {
			fail();
		}
	}

	@Test
	void interpretarArchivoXlsx() {
		try {
			excelFileParserService.generarListado("archivoDeEntrada/valido/plan_estudios.xlsx");
		} catch (FileException e) {
			fail();
		}
	}

	@Test
	void interpretarArchivoSinMaterias() {
		assertThrows(FileException.class, () -> excelFileParserService.generarListado("archivoDeEntrada/invalido/sin_materias.xls"));
	}

	@Test
	void interpretarArchivoSinNumerosDeMateria() {
		assertThrows(FileException.class, () -> excelFileParserService.generarListado("archivoDeEntrada/invalido/sin_numeros_de_materias.xls"));
	}

	@Test
	void interpretarArchivoAnioDeMateria() {
		assertThrows(FileException.class, () -> excelFileParserService.generarListado("archivoDeEntrada/invalido/sin_anio_de_materias.xls"));
	}

	@Test
	void planillaSemiValida1() throws FileException {
		excelFileParserService.generarListado("archivoDeEntrada/valido/prueba_1.xls");

		HashMap<Integer, Course> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(3, new Course(3, "Cuestiones de Sociología, Economía y Política", 2, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(592, new Course(592, "Introducción a la Problemática del Mundo Contemporáneo", 1, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1265, new Course(1265, "Análisis Matemático I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1269, new Course(1269, "Física II", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1240, new Course(1240, "Algoritmos y Programación III", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1242, new Course(1242, "Matemáticas Especiales", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1270, new Course(1270, "Física III", 3, CoursePeriod.PRIMER_CUATRIMESTRE));

		assertEquals(7, programRepository.countAll());
		assertEquals(listadoDeMaterias, programRepository.getProgramMap());
	}

	@Test
	void planillaValida1() throws FileException, InvalidCourseException {
		excelFileParserService.generarListado("archivoDeEntrada/valido/prueba_1.xlsx");

		HashMap<Integer, Course> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(3, new Course(3, "Cuestiones de Sociología, Economía y Política", 2, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(592, new Course(592, "Introducción a la Problemática del Mundo Contemporáneo", 1, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1265, new Course(1265, "Análisis Matemático I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(1269, new Course(1269, "Física II", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1240, new Course(1240, "Algoritmos y Programación III", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1242, new Course(1242, "Matemáticas Especiales", 2, CoursePeriod.SEGUNDO_CUATRIMESTRE));
		listadoDeMaterias.put(1270, new Course(1270, "Física III", 3, CoursePeriod.PRIMER_CUATRIMESTRE));

		assertEquals(7, programRepository.countAll());
		assertEquals(listadoDeMaterias, programRepository.getProgramMap());
		assertEquals(CourseStatus.NO_CURSADA, programRepository.findById(3).getCourseStatus());
		assertEquals(CourseStatus.APROBADA, programRepository.findById(592).getCourseStatus());
		assertEquals(CourseStatus.APROBADA, programRepository.findById(1265).getCourseStatus());
		assertEquals(CourseStatus.APROBADA, programRepository.findById(1269).getCourseStatus());
		assertEquals(CourseStatus.NO_CURSADA, programRepository.findById(1240).getCourseStatus());
		assertEquals(CourseStatus.REGULARIZADA, programRepository.findById(1242).getCourseStatus());
		assertEquals(CourseStatus.NO_CURSADA, programRepository.findById(1270).getCourseStatus());
		assertEquals("", programRepository.findById(3).getGrade());
		assertEquals("5", programRepository.findById(592).getGrade());
		assertEquals("6", programRepository.findById(1265).getGrade());
		assertEquals("8", programRepository.findById(1269).getGrade());
		assertEquals("", programRepository.findById(1240).getGrade());
		assertEquals("-", programRepository.findById(1242).getGrade());
		assertEquals("", programRepository.findById(1270).getGrade());
	}

	@Test
	void planillaSemiValida2() throws FileException {
		excelFileParserService.generarListado("archivoDeEntrada/valido/prueba_2.xls");

		HashMap<Integer, Course> listadoDeMaterias = new HashMap<>();
		listadoDeMaterias.put(3, new Course(3, "Cuestiones de Sociología, Economía y Política", 2, CoursePeriod.PRIMER_CUATRIMESTRE));
		listadoDeMaterias.put(15, new Course(15, "Análisis Matemático I", 1, CoursePeriod.PRIMER_CUATRIMESTRE));

		assertEquals(2, programRepository.countAll());
		assertEquals(listadoDeMaterias, programRepository.getProgramMap());
		assertEquals(CourseStatus.NO_CURSADA, programRepository.findById(3).getCourseStatus());
		assertEquals("", programRepository.findById(3).getGrade());
	}

}
