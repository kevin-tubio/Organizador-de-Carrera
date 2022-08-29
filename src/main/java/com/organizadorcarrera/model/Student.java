package com.organizadorcarrera.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STUDENT")
public class Student {

	@Id
	@Column
	private Integer id;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String career;

	public Student() { /* JPA exclusive */ }

	public Student(String firstName, String lastName, Integer studentId, String carrera) {
		this.firstName = capitalize(firstName);
		this.lastName = capitalize(lastName);
		this.id = studentId;
		this.career = carrera;
	}

	private String capitalize(String string) {
		return string.toUpperCase().replace(
				string.substring(1),
				string.substring(1).toLowerCase()
		);
	}

	public String getName() {
		return String.format("%s %s", this.firstName, this.lastName);
	}

	public Integer getId() {
		return id;
	}

	public String getCareer() {
		return career;
	}

	@Override
	public String toString() {
		return String.format("%s (%d).", getName(), id);
	}

}
