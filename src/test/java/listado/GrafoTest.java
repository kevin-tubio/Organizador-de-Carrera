package listado;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

import entity.Materia;
import enumerados.Periodo;
import excepciones.ListadoInvalidoException;
import excepciones.MateriaInvalidaException;
import util.LangResource;

public class GrafoTest {

	@Test
	public void secuenciaLineal() throws MateriaInvalidaException {
		var primera = new Materia(1, "Primera", 1, Periodo.PRIMER_CUATRIMESTRE);
		var segunda = new Materia(2, "Segunda", 1, Periodo.PRIMER_CUATRIMESTRE);
		var tercera = new Materia(3, "Tercera", 1, Periodo.PRIMER_CUATRIMESTRE);

		segunda.setCorrelativa(primera);
		tercera.setCorrelativa(segunda);
		Map<Integer, Materia> vertices = crearDiccionarioDeVertices(primera, segunda, tercera);
		var grafo = new Grafo();

		try {
			LinkedList<Materia> secuencia = new LinkedList<>(grafo.ordenamientoTopologico(vertices));
			assertEquals(secuencia.get(0), primera);
			assertEquals(secuencia.get(1), segunda);
			assertEquals(secuencia.get(2), tercera);
		} catch (ListadoInvalidoException e) {
			fail(); // NOSONAR
		}
	}

	@Test
	public void secuenciaBifurcada() throws MateriaInvalidaException {
		var primera = new Materia(1, "Primera", 1, Periodo.PRIMER_CUATRIMESTRE);
		var segunda = new Materia(2, "Segunda", 1, Periodo.PRIMER_CUATRIMESTRE);
		var tercera = new Materia(3, "Tercera", 1, Periodo.PRIMER_CUATRIMESTRE);
		var cuarta = new Materia(4, "Cuarta", 1, Periodo.PRIMER_CUATRIMESTRE);
		var quinta = new Materia(5, "Quinta", 1, Periodo.PRIMER_CUATRIMESTRE);
		var sexta = new Materia(6, "Sexta", 1, Periodo.PRIMER_CUATRIMESTRE);
		segunda.setCorrelativa(primera);
		tercera.setCorrelativa(segunda);
		quinta.setCorrelativa(primera);
		quinta.setCorrelativa(segunda);
		Map<Integer, Materia> vertices = crearDiccionarioDeVertices(primera, segunda, tercera, cuarta, quinta, sexta);
		var grafo = new Grafo();

		try {
			LinkedList<Materia> secuencia = new LinkedList<>(grafo.ordenamientoTopologico(vertices));
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
	public void secuenciaCiclica() throws MateriaInvalidaException {
		var primera = new Materia(1, "Primera", 1, Periodo.PRIMER_CUATRIMESTRE);
		var segunda = new Materia(2, "Segunda", 1, Periodo.PRIMER_CUATRIMESTRE);
		var tercera = new Materia(3, "Tercera", 1, Periodo.PRIMER_CUATRIMESTRE);
		primera.setCorrelativa(tercera);
		segunda.setCorrelativa(primera);
		tercera.setCorrelativa(segunda);
		Map<Integer, Materia> vertices = crearDiccionarioDeVertices(primera, segunda, tercera);
		var grafo = new Grafo();

		try {
			grafo.ordenamientoTopologico(vertices);
		} catch (ListadoInvalidoException e) {
			assertEquals(e.getMessage(), LangResource.getString("CorrelativasFormanCiclo"));
		}
	}

	private Map<Integer, Materia> crearDiccionarioDeVertices(Materia... materias) {
		Map<Integer, Materia> diccionario = new HashMap<>();
		for (Materia actual : materias) {
			diccionario.put(actual.getNumeroActividad(), actual);
		}

		return diccionario;
	}

}
