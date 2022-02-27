package com.organizadorcarrera.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.organizadorcarrera.enumerados.Estado;
import com.organizadorcarrera.enumerados.Periodo;
import com.organizadorcarrera.enumerados.Tipo;
import com.organizadorcarrera.exception.MateriaInvalidaException;
import com.organizadorcarrera.util.LangResource;

@Entity
@Table(name = "MATERIA", schema = "LISTADO")
public class Materia {

	@Id
	@Column(name = "ID", unique = true)
	private Integer numeroActividad;

	@Column(nullable = false)
	private String nombre;

	@Column(nullable = false)
	private Integer anio;

	@Column(nullable = false)
	private Integer calificacion;

	@Column(name = "HORAS_SEMANALES", nullable = false)
	private Integer horasSemanales;

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

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "MATERIA_MATERIA", joinColumns = @JoinColumn(name = "ID"), schema = "LISTADO")
	private Set<Materia> correlativas;

	public Materia() { /* JPA exclusive */ }

	public Materia(Integer numeroActividad, String nombre) {
		this(numeroActividad, nombre, 1, Periodo.PRIMER_CUATRIMESTRE);
	}

	public Materia(Integer numeroActividad, String nombre, Integer anio, Periodo periodo) {
		this(numeroActividad, nombre, anio, periodo, Estado.NO_CURSADA, 0);
	}

	public Materia(Integer numeroActividad, String nombre, Integer anio, Periodo periodo, Estado estado, Integer calificacion) {
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

	public Integer getNumeroActividad() {
		return numeroActividad;
	}

	public Integer getAnio() {
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

	public Integer getHorasSemanales() {
		return horasSemanales;
	}

	public void setHorasSemanales(Integer horasSemanales) {
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
		return numeroActividad.equals(other.numeroActividad);
	}

	@Override
	public String toString() {
		return nombre + " ( " + numeroActividad + " )";
	}

	public void setCalificacion(Integer calificacion) {
		this.calificacion = calificacion;
	}

	public String getCalificacion() {
		if (this.estado.equals(Estado.NO_CURSADA))
			return "";
		if (this.estado.equals(Estado.APROBADA))
			return String.valueOf(this.calificacion);
		return "-";
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
}
