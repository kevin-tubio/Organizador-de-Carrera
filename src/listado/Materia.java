package listado;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Materia {

	public enum Estado {
		NO_CURSADA, APROBADA, REGULARIZADA, REPROBADA
	}

	private int numeroActividad;
	private int anio;
	private int periodo;
	private String nombre;
	private Estado estado;
	private HashSet<Materia> correlativas;

	public Materia(int numeroActividad, String nombre, int anio, int periodo) {
		this.numeroActividad = numeroActividad;
		this.nombre = nombre;
		this.estado = Estado.NO_CURSADA;
		this.anio = anio;
		this.periodo = periodo;
		this.correlativas = new HashSet<>();
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

	public int getPeriodo() {
		return periodo;
	}

	public String getNombre() {
		return nombre;
	}

	public Set<Materia> getCorrelativas() {
		return correlativas;
	}

	public void setCorrelativas(Set<Materia> correlativas) {
		this.correlativas.addAll(correlativas);
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
		return "Materia [numeroActividad=" + numeroActividad + ", nombre=" + nombre + "]";
	}

}
