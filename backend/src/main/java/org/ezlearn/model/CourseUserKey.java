package org.ezlearn.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class CourseUserKey implements Serializable {
	private Long courseId;
	private Long userId;

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

}
