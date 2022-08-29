package com.organizadorcarrera.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.organizadorcarrera.repository.CourseRepository;
import com.organizadorcarrera.repository.ProgramRepository;
import com.organizadorcarrera.util.Grafo;
import com.organizadorcarrera.model.Course;
import com.organizadorcarrera.util.LangResource;
import com.organizadorcarrera.exception.InvalidCourseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgramService {

	private final Logger logger;
	private final Grafo grafo;
	private final CourseRepository courseRepository;
	private final ProgramRepository programRepository;

	@Autowired
	public ProgramService(CourseRepository materiaRepository, ProgramRepository programRepository, Grafo grafo) {
		this.logger = LoggerFactory.getLogger(ProgramService.class);
		this.courseRepository = materiaRepository;
		this.programRepository = programRepository;
		this.grafo = grafo;
	}

	public void clearProgram() {
		programRepository.deleteAll();
	}

	public void addCourse(Course course) {
		programRepository.save(course);
	}

	public void addCorrelatives(int course, int... correlatives) throws InvalidCourseException {
		validateCourses(course);
		validateCourses(correlatives);

		for (Integer correlative : correlatives) {
			try {
				programRepository.findById(course).setCorrelative(programRepository.findById(correlative));
			} catch (InvalidCourseException e) {
				logger.warn(String.format(LangResource.getString("MateriaInvalida"), course, e.getMessage()));
			}
		}
	}

	private void validateCourses(int... courses) throws InvalidCourseException {
		for (Integer course : courses) {
			if (!programRepository.existsById(course)) {
				throw new InvalidCourseException(String.format(
						LangResource.getString("MateriaInvalida"),
						course,
						LangResource.getString("MateriaNoEncontrada")
				));
			}
		}
	}

	@Override
	public String toString() {
		var stringBuilder = new StringBuilder();
		for (Course course : programRepository.getCourseList()) {
			stringBuilder.append(course.toString());
		}
		return stringBuilder.toString();
	}

	public Set<Course> getUnlockableCourses(int course) throws InvalidCourseException {
		validateCourses(course);
		return grafo.obtenerDesbloqueables(course, programRepository.getProgramMap());
	}

	public void deleteCourse(Course course) {
		try {
			validateCourses(course.getId());
			for (Course correlativa : getUnlockableCourses(course.getId()))
				correlativa.getCorrelatives().remove(course);
			programRepository.deleteById(course.getId());
		} catch (InvalidCourseException e) {
			logger.warn(e.getMessage());
		}
	}

	public void replaceCourse(Course oldCourse, Course newCourse) {
		try {
			for (Course correlative : getUnlockableCourses(oldCourse.getId())) {
				correlative.getCorrelatives().remove(oldCourse);
				correlative.getCorrelatives().add(newCourse);
			}
			programRepository.deleteById(oldCourse.getId());
			addCourse(newCourse);
		} catch (InvalidCourseException e) {
			logger.trace(e.getMessage(), e);
		}
	}

	public boolean containsCourse(String id) {
		try {
			return programRepository.existsById(Integer.valueOf(id));
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isEmpty() {
		return programRepository.isEmpty();
	}

	public void saveProgramCourses() {
		courseRepository.deleteAll();
		courseRepository.saveAll(programRepository.getProgramMap().values());
	}

	public void loadProgramCourses() {
		courseRepository.findAll().forEach(this::addCourse);

		if (this.isEmpty())
			logger.info("El listado esta vacio.");
	}

}
