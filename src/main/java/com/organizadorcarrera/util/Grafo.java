package com.organizadorcarrera.util;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.exception.ListadoInvalidoException;
import org.springframework.stereotype.Component;

@Component
public class Grafo {

	private final HashMap<Integer, Set<Course>> listaDeAdyacencia;

	public Grafo() {
		this.listaDeAdyacencia = new HashMap<>();
	}

	public List<Course> ordenamientoTopologico(Map<Integer, Course> vertices) throws ListadoInvalidoException {
		obtenerListaDeAdyacencia(vertices);
		HashMap<Integer, Integer> grados = new HashMap<>(obtenerGradosDeEntrada(vertices));
		ArrayDeque<Integer> cola = new ArrayDeque<>();
		LinkedList<Course> listaOrdenada = new LinkedList<>();

		for (Map.Entry<Integer, Integer> actual : grados.entrySet()) {
			if (actual.getValue() == 0) {
				cola.offer(actual.getKey());
			}
		}

		while (!cola.isEmpty()) {
			Course aProcesar = vertices.get(cola.poll());
			listaOrdenada.add(aProcesar);
			for (Course adyacente : this.listaDeAdyacencia.get(aProcesar.getId())) {
				int gradoActual = grados.get(adyacente.getId());
				grados.put(adyacente.getId(), gradoActual - 1);
				if (grados.get(adyacente.getId()) == 0) {
					cola.add(adyacente.getId());
				}
			}
		}

		if (listaOrdenada.size() != vertices.size()) {
			throw new ListadoInvalidoException(LangResource.getString("CorrelativasFormanCiclo"));
		}

		return listaOrdenada;
	}

	private void obtenerListaDeAdyacencia(Map<Integer, Course> vertices) {
		Set<Course> adyacentes = new HashSet<>();
		for (Map.Entry<Integer, Course> actual : vertices.entrySet()) {
			if (!this.listaDeAdyacencia.containsKey(actual.getKey())) {
				this.listaDeAdyacencia.put(actual.getKey(), new HashSet<>());
			}
			for (Course correlativa : actual.getValue().getCorrelatives()) {
				if (this.listaDeAdyacencia.containsKey(correlativa.getId())) {
					adyacentes = this.listaDeAdyacencia.get(correlativa.getId());
				}
				adyacentes.add(actual.getValue());
				this.listaDeAdyacencia.put(correlativa.getId(), adyacentes);
			}
		}
	}

	private Map<Integer, Integer> obtenerGradosDeEntrada(Map<Integer, Course> vertices) {
		Map<Integer, Integer> grados = new HashMap<>();
		for (Map.Entry<Integer, Course> materia : vertices.entrySet()) {
			grados.put(materia.getKey(), materia.getValue().getCorrelativesCount());
		}

		return grados;
	}

	public Set<Course> obtenerDesbloqueables(int materia, Map<Integer, Course> vertices) {
		if (!listaDeAdyacencia.containsKey(materia)) {
			obtenerListaDeAdyacencia(vertices);
		}
		return listaDeAdyacencia.get(materia);
	}

}
