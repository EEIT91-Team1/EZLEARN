package org.ezlearn.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class WishListId implements Serializable {
	
	private Long courseId;
	private Long userId;

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getuserId() {
		return userId;
	}

	public void setuserId(Long userId) {
		this.userId = userId;
	}
	
	public WishListId() {
		
	}
	
	public WishListId(Long courseId, Long userId) {
		this.courseId = courseId;
		this.userId = userId;
	}
	
}
