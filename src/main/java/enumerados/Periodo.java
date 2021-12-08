package enumerados;

public enum Periodo {
	PRIMER_CUATRIMESTRE("1er Cuatrimestre"), SEGUNDO_CUATRIMESTRE("2do Cuatrimestre"), ANUAL("Anual");

	private String tipo;

	private Periodo(String string) {
		this.tipo = string;
	}

	@Override
	public String toString() {
		return tipo;
	}
}
