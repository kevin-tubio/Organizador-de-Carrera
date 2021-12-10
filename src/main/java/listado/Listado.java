package listado;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import excepciones.ListadoInvalidoException;
import excepciones.MateriaInvalidaException;

public class Listado {

	private static Listado instancia;
	private static ObservableMap<Integer, Materia> listadoDeMaterias;
	private List<Materia> ordenDeMaterias;

	private Listado() {
		listadoDeMaterias = FXCollections.observableHashMap(); // NOSONAR
		this.ordenDeMaterias = null;
	}

	public static Listado obtenerListado() {
		if (instancia == null) {
			instancia = new Listado();
		}
		return instancia;
	}

	public static void borrarListado() {
		listadoDeMaterias.clear();
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
				System.err.println("La materia (" + materia + ") " + e.getMessage());
			}
		}
	}

	private void comprobarMaterias(int... materias) throws MateriaInvalidaException {
		for (Integer materia : materias) {
			if (!listadoDeMaterias.containsKey(materia)) {
				throw new MateriaInvalidaException("La materia (" + materia + ") no se encuentra dentro del listado");
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
			System.err.println(e.getMessage());
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
			System.err.println(e.getMessage());
		}
	}

}
