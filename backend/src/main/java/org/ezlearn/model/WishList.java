package org.ezlearn.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "wish_list")
public class WishList {
	
	@EmbeddedId
	private WishListId id;
	
	@Column(name="course_id", insertable = false, updatable = false)
	private Long courseId;
	
	@Column(name="user_id", insertable = false, updatable = false)
	private Long userId;
		
	public WishList() {}
	
	public WishListId getId() {
		return id;
	}
	public void setId(WishListId id) {
		this.id = id;
	}
	public Long getCourseId() {
		return courseId;
	}
	
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name="user_id")
	private Users users;

	@ManyToOne
	@MapsId("courseId")
	@JoinColumn(name="course_id")
	private Courses courses;
	
	public Users getUsers() {
		return users;
	}
	
	public void setUsers(Users users) {
		this.users = users;
	}
	
	public Courses getCourses() {
		return courses;
	}
	
	public void setCourses(Courses courses) {
		this.courses = courses;
	}
	
}

