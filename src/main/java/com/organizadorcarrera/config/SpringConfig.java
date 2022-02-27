package com.organizadorcarrera.config;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.organizadorcarrera.util.DirectorVentana;
import com.organizadorcarrera.util.WindowBuilder;

@Configuration
public class SpringConfig {

	@Bean
	public ResourceBundle getResourceBundle() {
		try {
			return ResourceBundle.getBundle("lang.string", Locale.getDefault());
		} catch (MissingResourceException e) {
			return ResourceBundle.getBundle("lang.string", new Locale("en"));
		}
	}

	@Bean
	public WindowBuilder getWindowBuilder() {
		return new WindowBuilder();
	}

	@Bean
	public DirectorVentana getDirectorVentana() {
		return new DirectorVentana();
	}
}
