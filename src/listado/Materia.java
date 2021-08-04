package listado;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import enumerados.Estado;
import excepciones.MateriaInvalidaException;

public class Materia {

	private int numeroActividad;
	private int anio;
	private String periodo;
	private int calificacion;
	private String nombre;
	private Estado estado;
	private HashSet<Materia> correlativas;

	public Materia(int numeroActividad, String nombre, int anio, String periodo) {
		this.numeroActividad = numeroActividad;
		this.nombre = nombre;
		this.estado = null;
		this.anio = anio;
		this.periodo = periodo;
		this.correlativas = new HashSet<>();
		this.calificacion = 0;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public int getNumeroActividad() {
		return numeroActividad;
	}

	public int getAnio() {
		return anio;
	}

	public String getPeriodo() {
		return periodo;
	}

	public String getNombre() {
		return nombre;
	}

	public Set<Materia> getCorrelativas() {
		return correlativas;
	}

	public void setCorrelativa(Materia correlativa) throws MateriaInvalidaException {
		if (!this.equals(correlativa)) {
			this.correlativas.add(correlativa);
		} else {
			throw new MateriaInvalidaException(
					"no puede tener como correlativa a (" + correlativa.numeroActividad + ").");
		}
	}

	public int cantidadDeCorrelativas() {
		return this.correlativas.size();
	}

	@Override
	public int hashCode() {
		return Objects.hash(numeroActividad);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Materia other = (Materia) obj;
		return numeroActividad == other.numeroActividad;
	}

	@Override
	public String toString() {
		return nombre + " (" + numeroActividad + ")";
	}

	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}

	public String getCalificacion() {
		switch (this.estado) {
		case APROBADA:
			return this.calificacion + "";
		case REGULARIZADA:
			return "Regularizada";
		default:
			return "";
		}
	}

}
