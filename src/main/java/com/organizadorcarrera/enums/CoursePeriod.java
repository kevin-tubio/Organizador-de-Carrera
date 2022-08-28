package com.organizadorcarrera.enums;

import com.organizadorcarrera.util.LangResource;

public enum CoursePeriod {

	PRIMER_CUATRIMESTRE("PeriodoPrimerCuatrimestre"),
	SEGUNDO_CUATRIMESTRE("PeriodoSegundoCuatrimestre"),
	ANUAL("PeriodoAnual");

	private final String tipo;

	CoursePeriod(String string) {
		this.tipo = LangResource.getString(string);
	}

	@Override
	public String toString() {
		return tipo;
	}
}
