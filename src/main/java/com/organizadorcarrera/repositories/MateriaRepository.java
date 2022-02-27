package com.organizadorcarrera.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.organizadorcarrera.entity.Materia;

@Repository
public interface MateriaRepository extends CrudRepository<Materia, Integer> {

}
