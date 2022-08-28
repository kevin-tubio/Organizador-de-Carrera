package com.organizadorcarrera.listado;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.enums.CoursePeriod;
import com.organizadorcarrera.exception.ListadoInvalidoException;
import com.organizadorcarrera.util.Grafo;
import com.organizadorcarrera.exception.InvalidCourseException;
import com.organizadorcarrera.util.LangResource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class GrafoTest {

	@Test
	void secuenciaLineal() throws InvalidCourseException {
		var primera = new Course(1, "Primera", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		var segunda = new Course(2, "Segunda", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		var tercera = new Course(3, "Tercera", 1, CoursePeriod.PRIMER_CUATRIMESTRE);

		segunda.setCorrelative(primera);
		tercera.setCorrelative(segunda);
		Map<Integer, Course> vertices = crearDiccionarioDeVertices(primera, segunda, tercera);
		var grafo = new Grafo();

		try {
			LinkedList<Course> secuencia = new LinkedList<>(grafo.ordenamientoTopologico(vertices));
			assertEquals(secuencia.get(0), primera);
			assertEquals(secuencia.get(1), segunda);
			assertEquals(secuencia.get(2), tercera);
		} catch (ListadoInvalidoException e) {
			fail(); // NOSONAR
		}
	}

	@Test
	void secuenciaBifurcada() throws InvalidCourseException {
		var primera = new Course(1, "Primera", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		var segunda = new Course(2, "Segunda", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		var tercera = new Course(3, "Tercera", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		var cuarta = new Course(4, "Cuarta", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		var quinta = new Course(5, "Quinta", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		var sexta = new Course(6, "Sexta", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		segunda.setCorrelative(primera);
		tercera.setCorrelative(segunda);
		quinta.setCorrelative(primera);
		quinta.setCorrelative(segunda);
		Map<Integer, Course> vertices = crearDiccionarioDeVertices(primera, segunda, tercera, cuarta, quinta, sexta);
		var grafo = new Grafo();

		try {
			LinkedList<Course> secuencia = new LinkedList<>(grafo.ordenamientoTopologico(vertices));
			assertEquals(secuencia.get(0), primera);
			assertEquals(secuencia.get(1), cuarta);
			assertEquals(secuencia.get(2), sexta);
			assertEquals(secuencia.get(3), segunda);
			assertEquals(secuencia.get(4), tercera);
			assertEquals(secuencia.get(5), quinta);
		} catch (ListadoInvalidoException e) {
			fail(); // NOSONAR
		}
	}

	@Test
	void secuenciaCiclica() throws InvalidCourseException {
		var primera = new Course(1, "Primera", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		var segunda = new Course(2, "Segunda", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		var tercera = new Course(3, "Tercera", 1, CoursePeriod.PRIMER_CUATRIMESTRE);
		primera.setCorrelative(tercera);
		segunda.setCorrelative(primera);
		tercera.setCorrelative(segunda);
		Map<Integer, Course> vertices = crearDiccionarioDeVertices(primera, segunda, tercera);
		var grafo = new Grafo();

		try {
			grafo.ordenamientoTopologico(vertices);
		} catch (ListadoInvalidoException e) {
			assertEquals(e.getMessage(), LangResource.getString("CorrelativasFormanCiclo"));
		}
	}

	private Map<Integer, Course> crearDiccionarioDeVertices(Course... materias) {
		Map<Integer, Course> diccionario = new HashMap<>();
		for (Course actual : materias) {
			diccionario.put(actual.getId(), actual);
		}

		return diccionario;
	}

}
