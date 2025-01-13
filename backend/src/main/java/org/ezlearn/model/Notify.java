package org.ezlearn.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Notify {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notifyId;
	@Column(insertable=false, updatable=false)
	private Long courseId;
	private String notifyContent;

	public Long getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(Long notifyId) {
		this.notifyId = notifyId;
	}


	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getNotifyContent() {
		return notifyContent;
	}

	public void setNotifyContent(String notifyContent) {
		this.notifyContent = notifyContent;
	}
	
	@OneToMany
	(mappedBy = "notify")
	private List<NotifyList> notifyLists;

	public List<NotifyList> getNotifyLists() {
		return notifyLists;
	}

	public void setNotifyLists(List<NotifyList> notifyLists) {
		this.notifyLists = notifyLists;
	}
	
	@ManyToOne
	@JoinColumn(name="courseId")
	private Courses courses;

	public Courses getCourses() {
		return courses;
	}
	public void setCourses(Courses courses) {
		this.courses = courses;
	}
}