package com.organizadorcarrera.listado;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.organizadorcarrera.entity.Materia;
import com.organizadorcarrera.exception.ListadoInvalidoException;
import com.organizadorcarrera.util.LangResource;

public class Grafo {

	private HashMap<Integer, Set<Materia>> listaDeAdyacencia;

	protected Grafo() {
		this.listaDeAdyacencia = new HashMap<>();
	}

	public List<Materia> ordenamientoTopologico(Map<Integer, Materia> vertices) throws ListadoInvalidoException {
		obtenerListaDeAdyacencia(vertices);
		HashMap<Integer, Integer> grados = new HashMap<>(obtenerGradosDeEntrada(vertices));
		ArrayDeque<Integer> cola = new ArrayDeque<>();
		LinkedList<Materia> listaOrdenada = new LinkedList<>();

		for (Map.Entry<Integer, Integer> actual : grados.entrySet()) {
			if (actual.getValue() == 0) {
				cola.offer(actual.getKey());
			}
		}

		while (!cola.isEmpty()) {
			Materia aProcesar = vertices.get(cola.poll());
			listaOrdenada.add(aProcesar);
			for (Materia adyacente : this.listaDeAdyacencia.get(aProcesar.getNumeroActividad())) {
				int gradoActual = grados.get(adyacente.getNumeroActividad());
				grados.put(adyacente.getNumeroActividad(), gradoActual - 1);
				if (grados.get(adyacente.getNumeroActividad()) == 0) {
					cola.add(adyacente.getNumeroActividad());
				}
			}
		}

		if (listaOrdenada.size() != vertices.size()) {
			throw new ListadoInvalidoException(LangResource.getString("CorrelativasFormanCiclo"));
		}

		return listaOrdenada;
	}

	private void obtenerListaDeAdyacencia(Map<Integer, Materia> vertices) {
		Set<Materia> adyacentes = new HashSet<>();
		for (Map.Entry<Integer, Materia> actual : vertices.entrySet()) {
			if (!this.listaDeAdyacencia.containsKey(actual.getKey())) {
				this.listaDeAdyacencia.put(actual.getKey(), new HashSet<>());
			}
			for (Materia correlativa : actual.getValue().getCorrelativas()) {
				if (this.listaDeAdyacencia.containsKey(correlativa.getNumeroActividad())) {
					adyacentes = this.listaDeAdyacencia.get(correlativa.getNumeroActividad());
				}
				adyacentes.add(actual.getValue());
				this.listaDeAdyacencia.put(correlativa.getNumeroActividad(), adyacentes);
			}
		}
	}

	private Map<Integer, Integer> obtenerGradosDeEntrada(Map<Integer, Materia> vertices) {
		Map<Integer, Integer> grados = new HashMap<>();
		for (Map.Entry<Integer, Materia> materia : vertices.entrySet()) {
			grados.put(materia.getKey(), materia.getValue().cantidadDeCorrelativas());
		}

		return grados;
	}

	public Set<Materia> obtenerDesbloqueables(int materia, Map<Integer, Materia> vertices) {
		if (!listaDeAdyacencia.containsKey(materia)) {
			obtenerListaDeAdyacencia(vertices);
		}
		return listaDeAdyacencia.get(materia);
	}

}
