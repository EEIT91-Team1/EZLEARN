package org.ezlearn.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Questions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long questionId;
	private String question;
	private String answer;
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	//----------------------
	@ManyToOne
	@JoinColumn(name = "lesson_id")
	private Lessons lesson;
	public Lessons getLesson() {
		return lesson;
	}
	public void setLesson(Lessons lesson) {
		this.lesson = lesson;
	}
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserInfo userInfo;
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	
}
