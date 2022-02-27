package com.organizadorcarrera.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.organizadorcarrera.listado.Listado;
import com.organizadorcarrera.repositories.MateriaRepository;

@Service
public class ListadoService {

	@Autowired
	private MateriaRepository materiaRepository;

	private Logger logger;

	public ListadoService() {
		this.logger = LoggerFactory.getLogger(ListadoService.class);
	}

	public void persistirCambiosListado() {
		materiaRepository.deleteAll();
		materiaRepository.saveAll(Listado.obtenerListado().getListadoDeMaterias().values());
	}

	public Listado recuperarListado() {
		var listado = Listado.obtenerListado();
		materiaRepository.findAll().forEach(listado::agregarMateria);

		if (listado.isEmpty())
			logger.info("El listado esta vacio.");

		return listado;
	}

}
