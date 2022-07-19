package com.organizadorcarrera.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.organizadorcarrera.builder.StageBuilder;
import com.organizadorcarrera.util.WindowDirector;

@Configuration
public class SpringConfig {

	@Bean
	public WindowDirector getWindowDirector() {
		return new WindowDirector();
	}

	@Bean
	public StageBuilder getStageBuilder() {
		return new StageBuilder();
	}

}
