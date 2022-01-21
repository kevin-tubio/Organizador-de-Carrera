package dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

public abstract class AccesadorADatos<T> {

	private EntityManager manager;
	private Class<T> clase;

	protected AccesadorADatos(Class<T> clase) {
		this.manager = Persistence.createEntityManagerFactory("OrganizadorDeCarrera").createEntityManager();
		this.clase = clase;
	}

	public Optional<T> obtener(int id) {
		return Optional.ofNullable(manager.find(clase, id));
	}

	public List<T> obtenerTodos() {
		return obtenerTodos("");
	}

	@SuppressWarnings("unchecked")
	public List<T> obtenerTodos(String condicion) {
		String qlString = "FROM " + clase.getName() + " " + condicion;
		Query query = manager.createQuery(qlString);
		return query.getResultList();
	}

	public boolean guardar(T dato) {
		ejecutarTransaccion(entityManager -> entityManager.persist(dato));
		return true;
	}

	public boolean actualizar(T dato) {
		ejecutarTransaccion(entityManager -> entityManager.merge(dato));
		return true;
	}

	public boolean borrar(T dato) {
		ejecutarTransaccion(entityManager -> entityManager.remove(dato));
		return true;
	}

	public void persistirTodo(Collection<T> values) {
		this.ejecutarTransaccion(em -> {
			values.forEach(em::persist);
			em.flush();
			em.clear();
		});
	}

	protected synchronized void ejecutarTransaccion(Consumer<EntityManager> action) {
		var tx = manager.getTransaction();
		try {
			tx.begin();
			action.accept(manager);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}
	}

	public EntityManager getManager() {
		return manager;
	}

}
