package org.ezlearn.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "purchased_courses")
public class PurchasedCourses {
	
	@EmbeddedId
	private PurchasedCoursesId purchasedCoursesId;
	
	private String courseReview;
	private Integer courseRate;
	

	public String getCourseReview() {
		return courseReview;
	}
	public void setCourseReview(String courseReview) {
		this.courseReview = courseReview;
	}
	public Integer getCourseRate() {
		return courseRate;
	}
	public void setCourseRate(Integer courseRate) {
		this.courseRate = courseRate;
	}
	public PurchasedCoursesId getPurchasedCoursesId() {
		return purchasedCoursesId;
	}
	public void setPurchasedCoursesId(PurchasedCoursesId purchasedCoursesId) {
		this.purchasedCoursesId = purchasedCoursesId;
	}

	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private Users users;
	
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	
	@ManyToOne
	@MapsId("courseId")
	@JoinColumn(name = "course_id")
	private Courses courses;

	public Courses getCourses() {
		return courses;
	}
	public void setCourses(Courses courses) {
		this.courses = courses;
	}

}
