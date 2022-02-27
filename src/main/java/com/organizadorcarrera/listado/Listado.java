package com.organizadorcarrera.listado;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import com.organizadorcarrera.entity.Materia;
import com.organizadorcarrera.util.LangResource;
import com.organizadorcarrera.exception.ListadoInvalidoException;
import com.organizadorcarrera.exception.MateriaInvalidaException;

public class Listado {

	private static Listado instancia;
	private ObservableMap<Integer, Materia> listadoDeMaterias;
	private List<Materia> ordenDeMaterias;
	private Logger logger;

	private Listado() {
		listadoDeMaterias = FXCollections.observableHashMap(); // NOSONAR
		this.ordenDeMaterias = null;
		this.logger = LoggerFactory.getLogger(Listado.class);
	}

	public static Listado obtenerListado() {
		if (instancia == null) {
			instancia = new Listado();
		}
		return instancia;
	}

	public static void borrarListado() {
		instancia.listadoDeMaterias.clear();
	}

	public void agregarMateria(Materia materia) {
		listadoDeMaterias.put(materia.getNumeroActividad(), materia);
	}

	public void agregarCorrelativas(int materia, int... correlativas) throws MateriaInvalidaException {
		comprobarMaterias(materia);
		comprobarMaterias(correlativas);

		for (Integer correlativa : correlativas) {
			try {
				listadoDeMaterias.get(materia).setCorrelativa(listadoDeMaterias.get(correlativa));
			} catch (MateriaInvalidaException e) {
				logger.warn(String.format(LangResource.getString("MateriaInvalida"), materia, e.getMessage()));
			}
		}
	}

	private void comprobarMaterias(int... materias) throws MateriaInvalidaException {
		for (Integer materia : materias) {
			if (!listadoDeMaterias.containsKey(materia)) {
				throw new MateriaInvalidaException(
						String.format(LangResource.getString("MateriaInvalida"), materia,
								LangResource.getString("MateriaNoEncontrada")));
			}
		}
	}

	@Override
	public String toString() {
		var salida = new StringWriter();
		for (Map.Entry<Integer, Materia> materia : listadoDeMaterias.entrySet()) {
			salida.write(materia.toString());
		}
		return salida.toString();
	}

	public void calcularOrdenDeMaterias() throws ListadoInvalidoException {
		Grafo grafo = new Grafo();
		ordenDeMaterias = new LinkedList<>(grafo.ordenamientoTopologico(listadoDeMaterias));
	}

	public Set<Materia> consultarDesbloqueables(int materia) throws MateriaInvalidaException {
		Grafo grafo = new Grafo();
		comprobarMaterias(materia);
		return grafo.obtenerDesbloqueables(materia, listadoDeMaterias);
	}

	public List<Materia> getOrdenDeMaterias() {
		return ordenDeMaterias;
	}

	public int consultarCantidadDeMaterias() {
		return listadoDeMaterias.size();
	}

	public ObservableMap<Integer, Materia> getListadoDeMaterias() {
		return listadoDeMaterias;
	}

	public Materia obtenerMateria(int numeroDeMateria) throws MateriaInvalidaException {
		comprobarMaterias(numeroDeMateria);
		return listadoDeMaterias.get(numeroDeMateria);
	}

	public void borrarMateria(Materia materia) {
		try {
			comprobarMaterias(materia.getNumeroActividad());
			for (Materia correlativa : consultarDesbloqueables(materia.getNumeroActividad()))
				correlativa.getCorrelativas().remove(materia);
			listadoDeMaterias.remove(materia.getNumeroActividad());
		} catch (MateriaInvalidaException e) {
			logger.warn(e.getMessage());
		}
	}

	public void reemplazarMateria(Materia materia, Materia nuevaMateria) {
		try {
			comprobarMaterias(materia.getNumeroActividad());
			for (Materia correlativa : consultarDesbloqueables(materia.getNumeroActividad())) {
				correlativa.getCorrelativas().remove(materia);
				correlativa.getCorrelativas().add(nuevaMateria);
			}
			listadoDeMaterias.remove(materia.getNumeroActividad());
			agregarMateria(nuevaMateria);
		} catch (MateriaInvalidaException e) {
			logger.trace(e.getMessage(), e);
		}
	}

	public boolean contieneMateria(String id) {
		try {
			return contieneMateria(Integer.valueOf(id));
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean contieneMateria(int id) {
		return listadoDeMaterias.containsKey(id);
	}

	public boolean isEmpty() {
		return listadoDeMaterias.isEmpty();
	}
}
