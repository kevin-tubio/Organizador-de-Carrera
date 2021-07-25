package listado;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import excepciones.MateriaInvalidaException;

public class Listado {

	private static Listado instancia;
	private HashMap<Integer, Materia> listadoDeMaterias;

	private Listado() {
		this.listadoDeMaterias = new HashMap<>();
	}

	public static Listado obtenerListado() {
		if (instancia == null) {
			instancia = new Listado();
		}
		return instancia;
	}

	public void agregarMateria(Materia materia) {
		this.listadoDeMaterias.put(materia.getNumeroActividad(), materia);
	}

	public void agregarCorrelativas(int materia, int... correlativas) throws MateriaInvalidaException {
		comprobarMaterias(materia);
		comprobarMaterias(correlativas);

		HashSet<Materia> conjuntoCorrelativas = new HashSet<>();
		for (Integer correlativa : correlativas) {
			conjuntoCorrelativas.add(listadoDeMaterias.get(correlativa));
		}
		listadoDeMaterias.get(materia).setCorrelativas(conjuntoCorrelativas);
	}

	private void comprobarMaterias(int... materias) throws MateriaInvalidaException {
		for (Integer materia : materias) {
			if (!listadoDeMaterias.containsKey(materia)) {
				throw new MateriaInvalidaException("La materia " + materia + " no se encuentra dentro del listado");
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

	public static void borrarListado() {
		Listado.instancia = null;
	}
}
