package org.ezlearn.model;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Progress {
	
	@EmbeddedId
	private ProgressId progressId;
	private Integer progressTime;
	private Integer totalDuration;
	private Integer progressPercent;
	private Boolean isCompleted;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public ProgressId getProgressId() {
		return progressId;
	}
	
	public void setProgressId(ProgressId progressId) {
		this.progressId = progressId;
	}
	
	public Integer getProgressTime() {
		return progressTime;
	}
	public void setProgressTime(Integer progressTime) {
		this.progressTime = progressTime;
	}
	public Integer getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(Integer totalDuration) {
		this.totalDuration = totalDuration;
	}
	public Integer getProgressPercent() {
		return progressPercent;
	}
	public void setProgressPercent(Integer progressPercent) {
		this.progressPercent = progressPercent;
	}
	
	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private Users users;
	
	public void setUsers(Users users) {
		this.users = users;
	}
	
	@ManyToOne
	@MapsId("lessonId")
	@JoinColumn(name = "lesson_id")
	private Lessons lessons;

	public void setLessons(Lessons lessons) {
		this.lessons = lessons;
	}

	public Lessons getLessons() {
		return lessons;
	}
	
}
