package com.organizadorcarrera.enumerados;

import com.organizadorcarrera.util.LangResource;

public enum CoursePeriod {

	PRIMER_CUATRIMESTRE("PeriodoPrimerCuatrimestre"),
	SEGUNDO_CUATRIMESTRE("PeriodoSegundoCuatrimestre"),
	ANUAL("PeriodoAnual");

	private String tipo;

	private CoursePeriod(String string) {
		this.tipo = LangResource.getString(string);
	}

	@Override
	public String toString() {
		return tipo;
	}
}
