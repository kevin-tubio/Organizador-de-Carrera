package com.organizadorcarrera.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ALUMNO", schema = "LISTADO")
public class Alumno {

	@Id
	@Column
	private Integer legajo;

	@Column(name = "nombre", nullable = false)
	private String nombreCompleto;

	@Column(nullable = false)
	private String carrera;

	public Alumno() { /* JPA exclusive */ }

	public Alumno(String nombreCompleto, Integer legajo, String carrera) {
		setNombreCompleto(nombreCompleto);
		this.legajo = legajo;
		this.carrera = carrera;
	}

	private void setNombreCompleto(String nombre) {
		this.nombreCompleto = capitalizarPalabras(nombre);
	}

	private String capitalizarPalabras(String nombre) {
		var sb = new StringBuilder();
		for (String palabra : nombre.split(" ")) {
			palabra = palabra.toUpperCase().replace(palabra.substring(1), palabra.substring(1).toLowerCase());
			sb.append(palabra + " ");
		}
		return sb.toString().stripTrailing();
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public Integer getLegajo() {
		return legajo.intValue();
	}

	public String getCarrera() {
		return carrera;
	}

	@Override
	public String toString() {
		return nombreCompleto + " (" + legajo + ").";
	}

}
