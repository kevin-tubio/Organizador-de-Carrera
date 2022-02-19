package com.organizadorcarrera.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.organizadorcarrera.controladores.ControladorAgregarMateria;
import com.organizadorcarrera.controladores.ControladorCierrePrograma;
import com.organizadorcarrera.controladores.ControladorLista;
import com.organizadorcarrera.controladores.ControladorPrincipal;
import com.organizadorcarrera.controladores.ControladorTabla;

@Configuration
public class SpringConfig {

	@Bean
	public ControladorPrincipal getControladorPrincipal() {
		return new ControladorPrincipal();
	}

	@Bean
	public ControladorTabla getControladorTabla() {
		return new ControladorTabla();
	}

	@Bean
	public ControladorLista getControladorLista() {
		return new ControladorLista();
	}

	@Bean
	public ControladorAgregarMateria getControladorAgregarMateria() {
		return new ControladorAgregarMateria();
	}

	@Bean
	public ControladorCierrePrograma getControladorCierrePrograma() {
		return new ControladorCierrePrograma();
	}
}
