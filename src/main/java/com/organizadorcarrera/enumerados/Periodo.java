package com.organizadorcarrera.enumerados;

import com.organizadorcarrera.util.LangResource;

public enum Periodo {
	PRIMER_CUATRIMESTRE("PeriodoPrimerCuatrimestre"), SEGUNDO_CUATRIMESTRE("PeriodoSegundoCuatrimestre"),
	ANUAL("PeriodoAnual");

	private String tipo;

	private Periodo(String string) {
		this.tipo = LangResource.getString(string);
	}

	@Override
	public String toString() {
		return tipo;
	}
}
