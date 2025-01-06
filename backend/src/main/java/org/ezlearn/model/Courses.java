package org.ezlearn.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Courses {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseId;
	private Integer price;
	private String courseName;
	private String courseIntro;
	private String courseType;
	private byte[] courseImg;

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseIntro() {
		return courseIntro;
	}

	public void setCourseIntro(String courseIntro) {
		this.courseIntro = courseIntro;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public byte[] getCourseImg() {
		return courseImg;
	}

	public void setCourseImg(byte[] courseImg) {
		this.courseImg = courseImg;
	}

	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private UserInfo userInfo;

	public UserInfo getTeachers() {
		return userInfo;
	}

	public void setTeachers(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
