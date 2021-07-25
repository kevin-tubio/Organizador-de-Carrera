package listado;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

import excepciones.ListadoInvalidoException;

public class GrafoTest {

	@Test
	public void secuenciaLineal() {
		var primera = new Materia(1, "Primera", 1, 1);
		var segunda = new Materia(2, "Segunda", 1, 1);
		var tercera = new Materia(3, "Tercera", 1, 1);
		HashSet<Materia> cSegunda = new HashSet<>();
		HashSet<Materia> cTercera = new HashSet<>();
		cSegunda.add(primera);
		segunda.setCorrelativas(cSegunda);
		cTercera.add(segunda);
		tercera.setCorrelativas(cTercera);
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
	public void secuenciaBifurcada() {
		var primera = new Materia(1, "Primera", 1, 1);
		var segunda = new Materia(2, "Segunda", 1, 1);
		var tercera = new Materia(3, "Tercera", 1, 1);
		var cuarta = new Materia(4, "Cuarta", 1, 1);
		var quinta = new Materia(5, "Quinta", 1, 1);
		var sexta = new Materia(6, "Sexta", 1, 1);
		HashSet<Materia> cSegunda = new HashSet<>();
		HashSet<Materia> cTercera = new HashSet<>();
		HashSet<Materia> cQuinta = new HashSet<>();
		cSegunda.add(primera);
		segunda.setCorrelativas(cSegunda);
		cTercera.add(segunda);
		tercera.setCorrelativas(cTercera);
		cQuinta.add(primera);
		cQuinta.add(segunda);
		quinta.setCorrelativas(cQuinta);
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
	public void secuenciaCiclica() {
		var primera = new Materia(1, "Primera", 1, 1);
		var segunda = new Materia(2, "Segunda", 1, 1);
		var tercera = new Materia(3, "Tercera", 1, 1);
		HashSet<Materia> cPrimera = new HashSet<>();
		HashSet<Materia> cSegunda = new HashSet<>();
		HashSet<Materia> cTercera = new HashSet<>();
		cPrimera.add(tercera);
		primera.setCorrelativas(cPrimera);
		cSegunda.add(primera);
		segunda.setCorrelativas(cSegunda);
		cTercera.add(segunda);
		tercera.setCorrelativas(cTercera);
		Map<Integer, Materia> vertices = crearDiccionarioDeVertices(primera, segunda, tercera);
		var grafo = new Grafo();

		try {
			grafo.ordenamientoTopologico(vertices);
		} catch (ListadoInvalidoException e) {
			assertEquals(e.getMessage(), "Las correlativas a las materias del listado forman un ciclo");
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
