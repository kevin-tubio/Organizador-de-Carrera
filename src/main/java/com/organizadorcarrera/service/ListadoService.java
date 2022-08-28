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

	@Autowired
	public ListadoService(CourseRepository materiaRepository) {
		this.materiaRepository = materiaRepository;
		this.logger = LoggerFactory.getLogger(ListadoService.class);
	}

	public void persistirCambiosListado() {
		materiaRepository.deleteAll();
		materiaRepository.saveAll(Program.getInstance().getProgramMap().values());
	}

	public void recuperarListado() {
		var listado = Program.getInstance();
		materiaRepository.findAll().forEach(listado::addCourse);

		if (listado.isEmpty())
			logger.info("El listado esta vacio.");
	}

}
