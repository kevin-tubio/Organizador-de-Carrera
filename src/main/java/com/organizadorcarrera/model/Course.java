package com.organizadorcarrera.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.organizadorcarrera.enums.CourseStatus;
import com.organizadorcarrera.enums.CoursePeriod;
import com.organizadorcarrera.enums.CourseType;
import com.organizadorcarrera.exception.InvalidCourseException;
import com.organizadorcarrera.util.LangResource;

@Entity
@Table(name = "MATERIA", schema = "LISTADO")
public class Course {

	@Id
	@Column(name = "ID", unique = true)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer year;

	@Column(nullable = false)
	private Integer grade;

	@Column(name = "HORAS_SEMANALES", nullable = false)
	private Integer hours;

	@Column(nullable = false)
	private double credits;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CoursePeriod coursePeriod;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CourseStatus courseStatus;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CourseType courseType;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "MATERIA_MATERIA", joinColumns = @JoinColumn(name = "ID"), schema = "LISTADO")
	private Set<Course> correlatives;

	public Course() { /* JPA exclusive */ }

	public Course(Integer id, String name) {
		this(id, name, 1, CoursePeriod.PRIMER_CUATRIMESTRE);
	}

	public Course(Integer id, String name, Integer year, CoursePeriod coursePeriod) {
		this(id, name, year, coursePeriod, CourseStatus.NO_CURSADA, -1);
	}

	public Course(Integer id, String name, Integer year, CoursePeriod coursePeriod, CourseStatus courseStatus, Integer grade) {
		this.grade = grade;
		this.id = id;
		this.name = name;
		setCourseStatus(courseStatus);
		this.year = year;
		this.coursePeriod = coursePeriod;
		this.correlatives = new HashSet<>();
		this.hours = 0;
		this.credits = 0;
		this.courseType = CourseType.MATERIA;
	}

	public CourseStatus getCourseStatus() {
		return this.courseStatus;
	}

	public void setCourseStatus(CourseStatus courseStatus) {
		if (courseStatus.equals(CourseStatus.APROBADA) && this.grade.intValue() == -1)
			this.grade = 4;
		else if (!courseStatus.equals(CourseStatus.APROBADA))
			this.grade = -1;
		this.courseStatus = courseStatus;
	}

	public Integer getId() {
		return this.id;
	}

	public Integer getYear() {
		return this.year;
	}

	public CoursePeriod getCoursePeriod() {
		return this.coursePeriod;
	}

	public String getName() {
		return this.name;
	}

	public Set<Course> getCorrelatives() {
		return this.correlatives;
	}

	public void setCorrelatives(Set<Course> correlatives) {
		this.correlatives = correlatives;
	}

	public void setCorrelative(Course correlative) throws InvalidCourseException {
		if (this.equals(correlative))
			throw new InvalidCourseException(String.format(LangResource.getString("CorrelativaInvalida"), correlative.id));

		this.correlatives.add(correlative);
	}

	public int getCorrelativesCount() {
		return this.correlatives.size();
	}

	public CourseType getCourseType() {
		return courseType;
	}

	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}

	public Integer getHours() {
		return this.hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public double getCredits() {
		return this.credits;
	}

	public void setCreditos(double creditos) {
		this.credits = creditos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		return id.equals(other.id);
	}

	@Override
	public String toString() {
		return String.format("%s ( %d )", name, id);
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getGrade() {
		if (this.courseStatus.equals(CourseStatus.NO_CURSADA))
			return "";
		if (this.courseStatus.equals(CourseStatus.APROBADA))
			return String.valueOf(this.grade);
		return "-";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setCoursePeriod(CoursePeriod coursePeriod) {
		this.coursePeriod = coursePeriod;
	}
}
