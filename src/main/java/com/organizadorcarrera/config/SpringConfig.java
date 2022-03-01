package com.organizadorcarrera.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.organizadorcarrera.builder.WindowBuilder;
import com.organizadorcarrera.util.WindowDirector;

@Configuration
public class SpringConfig {

	@Bean
	public WindowBuilder getWindowBuilder() {
		return new WindowBuilder();
	}

	@Bean
	public WindowDirector getWindowDirector() {
		return new WindowDirector();
	}
}
