package entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import enumerados.Estado;
import enumerados.Periodo;
import enumerados.Tipo;
import excepciones.MateriaInvalidaException;
import util.LangResource;

@Entity
@Table(name = "MATERIA", schema = "LISTADO")
public class Materia {

	@Id
	@Column(name = "ID", unique = true)
	private int numeroActividad;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	private int anio;

	@Column(nullable = false)
	private int calificacion;

	@Column(name = "HORAS_SEMANALES", nullable = false)
	private int horasSemanales;

	@Column(nullable = false)
	private double creditos;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Periodo periodo;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Estado estado;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Tipo tipo;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "MATERIA_MATERIA", joinColumns = @JoinColumn(name = "ID"), schema = "LISTADO")
	private Set<Materia> correlativas;

	public Materia() {
		// declarado solo para jpa
	}

	public Materia(int numeroActividad, String nombre) {
		this(numeroActividad, nombre, 1, Periodo.PRIMER_CUATRIMESTRE);
	}

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

	public void setCorrelativas(Set<Materia> correlativas) {
		this.correlativas = correlativas;
	}

	public void setCorrelativa(Materia correlativa) throws MateriaInvalidaException {
		if (!this.equals(correlativa)) {
			this.correlativas.add(correlativa);
		} else {
			throw new MateriaInvalidaException(
					String.format(LangResource.getString("CorrelativaInvalida"), correlativa.numeroActividad));
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
		return nombre + " ( " + numeroActividad + " )";
	}

	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}

	public String getCalificacion() {
		switch (this.estado) {
		case APROBADA:
			return this.calificacion + "";
		case REGULARIZADA:
			return Estado.REGULARIZADA.toString();
		default:
			return Estado.NO_CURSADA.toString();
		}
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
}
