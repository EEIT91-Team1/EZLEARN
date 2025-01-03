package org.ezlearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Purchasedcourse {

	@EmbeddedId
	private CourseUserKey id;

	@Column(name = "courserate")
	private Integer courseRate;

	public CourseUserKey getId() {
		return id;
	}

	public void setId(CourseUserKey id) {
		this.id = id;
	}

	public Integer getCourseRate() {
		return courseRate;
	}

	public void setCourseRate(Integer courseRate) {
		this.courseRate = courseRate;
	}

	@ManyToOne
	@MapsId("courseId")
	@JoinColumn(name = "courseid")
	@JsonBackReference
	private Courses courses;

	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "userid")
	@JsonBackReference
	private Users users;

	public Courses getCourses() {
		return courses;
	}

	public void setCourses(Courses courses) {
		this.courses = courses;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

}
