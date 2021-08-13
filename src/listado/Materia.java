package listado;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import enumerados.Estado;
import enumerados.Periodo;
import enumerados.Tipo;
import excepciones.MateriaInvalidaException;

public class Materia {

	private int numeroActividad;
	private String nombre;
	private int anio;
	private int calificacion;
	private int horasSemanales;
	private double creditos;
	private Periodo periodo;
	private Estado estado;
	private Tipo tipo;
	private Set<Materia> correlativas;

	public Materia(int numeroActividad, String nombre, int anio, Periodo periodo) {
		this(numeroActividad, nombre, anio, periodo, Estado.NO_CURSADA, 0);
	}

	public Materia(int numeroActividad, String nombre, int anio, Periodo periodo, Estado estado, int calificacion) {
		this.numeroActividad = numeroActividad;
		this.nombre = nombre;
		this.estado = estado;
		this.anio = anio;
		this.periodo = periodo;
		this.correlativas = new HashSet<>();
		this.calificacion = calificacion;
		this.horasSemanales = 0;
		this.creditos = 0;
		this.tipo = Tipo.MATERIA;
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

	public Periodo getPeriodo() {
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

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public int getHorasSemanales() {
		return horasSemanales;
	}

	public void setHorasSemanales(int horasSemanales) {
		this.horasSemanales = horasSemanales;
	}

	public double getCreditos() {
		return creditos;
	}

	public void setCreditos(double creditos) {
		this.creditos = creditos;
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
			return "Aprobado";
		default:
			return "";
		}
	}

}
