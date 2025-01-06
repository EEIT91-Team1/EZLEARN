package org.ezlearn.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class PurchasedCoursesId implements Serializable {

	private Long userId;
	private Long courseId;
	
	public PurchasedCoursesId() {}
	public PurchasedCoursesId(Long userId, Long courseId) {
		super();
		this.userId = userId;
		this.courseId = courseId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getCourseId() {
		return courseId;
	}
	
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(courseId, userId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PurchasedCoursesId other = (PurchasedCoursesId) obj;
		return Objects.equals(courseId, other.courseId) && Objects.equals(userId, other.userId);
	}
	
}
