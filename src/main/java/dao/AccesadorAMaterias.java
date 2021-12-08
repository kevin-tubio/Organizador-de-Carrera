package dao;

import listado.Materia;

public class AccesadorAMaterias extends AccesadorADatos<Materia> {

	public AccesadorAMaterias() {
		super(Materia.class);
	}

	private void borrarCorrelativas() {
		String qlString = "TRUNCATE TABLE LISTADO.MATERIA_MATERIA";
		ejecutarTransaccion(entityManager -> entityManager.createNativeQuery(qlString).executeUpdate());
	}

	@Override
	public void borrarTodo() {
		borrarCorrelativas();
		super.borrarTodo();
	}
}
