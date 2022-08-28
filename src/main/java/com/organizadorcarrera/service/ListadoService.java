package com.organizadorcarrera.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.organizadorcarrera.program.Program;
import com.organizadorcarrera.repository.CourseRepository;

@Service
public class ListadoService {

	private final CourseRepository materiaRepository;
	private final Logger logger;
	private final Program program;

	@Autowired
	public ListadoService(CourseRepository materiaRepository, Program program) {
		this.materiaRepository = materiaRepository;
		this.logger = LoggerFactory.getLogger(ListadoService.class);
		this.program = program;
	}

	public void persistirCambiosListado() {
		materiaRepository.deleteAll();
		materiaRepository.saveAll(program.getProgramMap().values());
	}

	public void recuperarListado() {
		materiaRepository.findAll().forEach(program::addCourse);

		if (program.isEmpty())
			logger.info("El listado esta vacio.");
	}

}
