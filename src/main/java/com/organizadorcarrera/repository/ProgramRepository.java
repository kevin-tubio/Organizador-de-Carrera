package com.organizadorcarrera.repository;

import com.organizadorcarrera.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProgramRepository {

    private final ObservableMap<Integer, Course> courseMap;

    public ProgramRepository() {
        this.courseMap = FXCollections.observableHashMap();
    }

    public void deleteAll() {
        this.courseMap.clear();
    }

    public void deleteById(Integer courseId) {
        courseMap.remove(courseId);
    }

    public void save(Course course) {
        courseMap.put(course.getId(), course);
    }

    public Course findById(Integer courseId) {
        return courseMap.get(courseId);
    }

    public ObservableMap<Integer, Course> getProgramMap() {
        return courseMap;
    }

    public List<Course> getCourseList() {
        return new ArrayList<>(courseMap.values());
    }

    public boolean existsById(Integer courseId) {
        return courseMap.containsKey(courseId);
    }

    public int countAll() {
        return courseMap.size();
    }

    public boolean isEmpty() {
        return courseMap.isEmpty();
    }

}
