package org.ezlearn.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProgressId implements Serializable {
	
	private Long userId;
	private Long lessonId;
	
	public ProgressId() {}
	public ProgressId(Long userId, Long lessonId) {
		super();
		this.userId = userId;
		this.lessonId = lessonId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getLessonId() {
		return lessonId;
	}
	
	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(lessonId, userId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProgressId other = (ProgressId) obj;
		return Objects.equals(lessonId, other.lessonId) && Objects.equals(userId, other.userId);
	}

}
