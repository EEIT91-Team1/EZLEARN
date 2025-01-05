package org.ezlearn.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Notes {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long noteId;
	private String noteTitle;
	private String noteContent;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	
	
	public Long getNoteId() {
		return noteId;
	}
	
	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}
	
	public String getNoteTitle() {
		return noteTitle;
	}
	
	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}
	
	public String getNoteContent() {
		return noteContent;
	}
	
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}
	
	public LocalDate getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	
	public LocalDate getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users users;
	
	public void setUsers(Users users) {
		this.users = users;
	}

	public Users getUsers() {
		return users;
	}

	@ManyToOne
	@JoinColumn(name = "lesson_id")
	private Lessons lessons;

	public Lessons getLessons() {
		return lessons;
	}

	public void setLessons(Lessons lessons) {
		this.lessons = lessons;
	}

}
