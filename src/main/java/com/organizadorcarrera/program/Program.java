package com.organizadorcarrera.program;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import com.organizadorcarrera.entity.Course;
import com.organizadorcarrera.util.LangResource;
import com.organizadorcarrera.exception.ListadoInvalidoException;
import com.organizadorcarrera.exception.InvalidCourseException;

public class Program {

	private static Program instance;
	private ObservableMap<Integer, Course> programMap;
	private List<Course> courseOrderedList;
	private Logger logger;

	private Program() {
		programMap = FXCollections.observableHashMap(); // NOSONAR
		this.courseOrderedList = null;
		this.logger = LoggerFactory.getLogger(Program.class);
	}

	public static Program getInstance() {
		if (instance == null) {
			instance = new Program();
		}
		return instance;
	}

	public static void clearProgram() {
		instance.programMap.clear();
	}

	public void addCourse(Course course) {
		programMap.put(course.getId(), course);
	}

	public void addCorrelatives(int course, int... correlatives) throws InvalidCourseException {
		validateCourses(course);
		validateCourses(correlatives);

		for (Integer correlative : correlatives) {
			try {
				programMap.get(course).setCorrelative(programMap.get(correlative));
			} catch (InvalidCourseException e) {
				logger.warn(String.format(LangResource.getString("MateriaInvalida"), course, e.getMessage()));
			}
		}
	}

	private void validateCourses(int... courses) throws InvalidCourseException {
		for (Integer course : courses) {
			if (!programMap.containsKey(course)) {
				throw new InvalidCourseException(
						String.format(LangResource.getString("MateriaInvalida"), course,
								LangResource.getString("MateriaNoEncontrada")));
			}
		}
	}

	@Override
	public String toString() {
		var writer = new StringWriter();
		for (Map.Entry<Integer, Course> course : programMap.entrySet()) {
			writer.write(course.toString());
		}
		return writer.toString();
	}

	public void orderCourses() throws ListadoInvalidoException {
		Grafo grafo = new Grafo();
		courseOrderedList = new LinkedList<>(grafo.ordenamientoTopologico(programMap));
	}

	public Set<Course> getUnlockableCourses(int course) throws InvalidCourseException {
		Grafo grafo = new Grafo();
		validateCourses(course);
		return grafo.obtenerDesbloqueables(course, programMap);
	}

	public List<Course> getCourseOrderedList() {
		return courseOrderedList;
	}

	public int getCoursesCount() {
		return programMap.size();
	}

	public ObservableMap<Integer, Course> getProgramMap() {
		return programMap;
	}

	public Course getCourse(int id) throws InvalidCourseException {
		validateCourses(id);
		return programMap.get(id);
	}

	public void deleteCourse(Course course) {
		try {
			validateCourses(course.getId());
			for (Course correlativa : getUnlockableCourses(course.getId()))
				correlativa.getCorrelatives().remove(course);
			programMap.remove(course.getId());
		} catch (InvalidCourseException e) {
			logger.warn(e.getMessage());
		}
	}

	public void replaceCourse(Course oldCourse, Course newCourse) {
		try {
			validateCourses(oldCourse.getId());
			for (Course correlative : getUnlockableCourses(oldCourse.getId())) {
				correlative.getCorrelatives().remove(oldCourse);
				correlative.getCorrelatives().add(newCourse);
			}
			programMap.remove(oldCourse.getId());
			addCourse(newCourse);
		} catch (InvalidCourseException e) {
			logger.trace(e.getMessage(), e);
		}
	}

	public boolean containsCourse(String id) {
		try {
			return programMap.containsKey(Integer.valueOf(id));
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isEmpty() {
		return programMap.isEmpty();
	}
}
