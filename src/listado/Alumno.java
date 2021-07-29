package listado;

public class Alumno {
	
	private String nombreCompleto;
	private int legajo;
	private String carrera;
	
	public Alumno(String nombreCompleto, int legajo, String carrera) {
		this.nombreCompleto = nombreCompleto;
		this.legajo = legajo;
		this.carrera = carrera;
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

}
