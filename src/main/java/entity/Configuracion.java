package entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import enumerados.TipoConfiguracion;

@Entity
@Table(name = "CONFIG", schema = "LISTADO")
public class Configuracion {

	@Id
	@Column(nullable = false, unique = true)
	private final String parametro;

	@Column(nullable = false, unique = false)
	private final String valor;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private final TipoConfiguracion tipo;
	
	public Configuracion() {
		this.parametro = null;
		this.valor = null;
		this.tipo = null;
	}

	public Configuracion(String parametro, String valor, TipoConfiguracion tipo) {
		this.parametro = parametro;
		this.valor = valor;
		this.tipo = tipo;
	}

	public String getParametro() {
		return parametro;
	}

	public String getValor() {
		return valor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(parametro, tipo, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Configuracion))
			return false;
		Configuracion other = (Configuracion) obj;
		return parametro.equals(other.parametro) && tipo.equals(other.tipo) && valor.equals(other.valor);
	}

}
