package com.organizadorcarrera.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.organizadorcarrera.model.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, Integer> {

}
