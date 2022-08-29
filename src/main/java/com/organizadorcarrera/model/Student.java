package com.organizadorcarrera.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ALUMNO")
public class Student {

	@Id
	@Column
	private Integer legajo;

	@Column(name = "nombre", nullable = false)
	private String nombreCompleto;

	@Column(nullable = false)
	private String carrera;

	public Student() { /* JPA exclusive */ }

	public Student(String nombreCompleto, Integer legajo, String carrera) {
		setNombreCompleto(nombreCompleto);
		this.legajo = legajo;
		this.carrera = carrera;
	}

	private void setNombreCompleto(String nombre) {
		this.nombreCompleto = capitalizarPalabras(nombre);
	}

	private String capitalizarPalabras(String nombre) {
		var stringBuilder = new StringBuilder();
		for (String palabra : nombre.split(" ")) {
			stringBuilder.append(palabra.toUpperCase().replace(palabra.substring(1), palabra.substring(1).toLowerCase()));
			stringBuilder.append(" ");
		}

		return stringBuilder.toString().stripTrailing();
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public Integer getLegajo() {
		return legajo;
	}

	public String getCarrera() {
		return carrera;
	}

	@Override
	public String toString() {
		return String.format("%s (%d).", nombreCompleto, legajo);
	}

}
