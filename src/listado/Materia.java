package listado;

import java.util.LinkedList;
import java.util.List;

public class Materia {

	public enum Estado {
		NO_CURSADA, APROBADA, REGULARIZADA, REPROBADA
	}

	private int numeroActividad;
	private int anio;
	private int periodo;
	private String nombre;
	private Estado estado;
	private LinkedList<Materia> correlativas;

	public Materia(int numeroActividad, String nombre, int anio, int periodo) {
		this.numeroActividad = numeroActividad;
		this.nombre = nombre;
		this.estado = Estado.NO_CURSADA;
		this.anio = anio;
		this.periodo = periodo;
		this.correlativas = new LinkedList<>();
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

	public List<Materia> getCorrelativas() {
		return correlativas;
	}

	public void setCorrelativas(List<Materia> correlativas) {
		this.correlativas.addAll(correlativas);
	}

	public int cantidadDeCorrelativas() {
		return this.correlativas.size();
	}
}
