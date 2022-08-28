package com.organizadorcarrera.config;

import com.organizadorcarrera.entity.Course;

import com.organizadorcarrera.program.Program;
import io.reactivex.disposables.CompositeDisposable;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;

import javafx.collections.transformation.FilteredList;
import net.synedra.validatorfx.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SpringConfig {

    @Bean
    @Scope("prototype")
    public CompositeDisposable getCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Bean
    @Scope("prototype")
    public Validator getValidator() {
        return new Validator();
    }

    @Bean(name = "courseList")
    public ObservableList<Course> getObsevableCourses(Program program) {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        MapChangeListener<Integer, Course> listener = change -> courseList.setAll(change.getMap().values());
        program.getProgramMap().addListener(listener);
        return courseList;
    }

    @Bean(name = "filteredCourseList")
    @Scope("prototype")
    public FilteredList<Course> getFilteredList(Program program) {
        return new FilteredList<>(getObsevableCourses(program));
    }

    @Bean(name = "correlativeCourses")
    @Scope("prototype")
    public ObservableList<Course> getCorrelativeCoursesList() {
        return FXCollections.observableArrayList();
    }

}
