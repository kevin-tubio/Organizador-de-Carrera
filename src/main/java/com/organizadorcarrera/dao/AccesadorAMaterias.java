package com.organizadorcarrera.dao;

import com.organizadorcarrera.entity.Materia;

public class AccesadorAMaterias extends AccesadorADatos<Materia> {

	public AccesadorAMaterias() {
		super(Materia.class);
	}

	private void borrarCorrelativas() {
		String qlString = "TRUNCATE TABLE LISTADO.MATERIA_MATERIA";
		ejecutarTransaccion(entityManager -> entityManager.createNativeQuery(qlString).executeUpdate());
	}

	public void borrarTodo() {
		borrarCorrelativas();
		String qlString = "SET REFERENTIAL_INTEGRITY FALSE; TRUNCATE TABLE LISTADO.MATERIA; SET REFERENTIAL_INTEGRITY TRUE;";
		ejecutarTransaccion(entityManager -> entityManager.createNativeQuery(qlString).executeUpdate());
	}
}
