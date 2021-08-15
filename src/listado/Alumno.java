package listado;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Alumno {

	@Id
	@Column
	private int legajo;
	@Column(name = "nombre", nullable = false)
	private String nombreCompleto;
	@Column(nullable = false)
	private String carrera;

	public Alumno(String nombreCompleto, int legajo, String carrera) {
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

	public int getLegajo() {
		return legajo;
	}

	public String getCarrera() {
		return carrera;
	}

	@Override
	public String toString() {
		return nombreCompleto + " (" + legajo + ").";
	}

}
