package enumerados;

public enum Tipo {
	MATERIA("Materia"), TESIS("Tesis"), SEMINARIO_ELECTIVO("Seminario electivo"),
	SEMINARIO_OPTATIVO("Seminario optativo");

	private String mensaje;

	private Tipo(String mensaje) {
		this.mensaje = mensaje;
	}

	@Override
	public String toString() {
		return this.mensaje;
	}
}
