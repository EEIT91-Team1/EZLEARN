package org.ezlearn.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
	@Column(name = "course_id")
	private Long courseId;
	@Column(name = "price")
	private int price;
	@Column(name = "course_name")
	private String courseName;
	@Column(name = "course_intro")
	private String courseIntro;
	@Column(name = "course_img")
	private byte[] courseImg;
	@Column(name = "course_type")
	private String courseType;
	@Transient
	private String courseImgBase64;
	@Transient
	private String courseRates;

	public String getCourseRates() {
		return courseRates;
	}

	public void setCouseRates(String couseRates) {
		this.courseRates = couseRates;
	}

	public String getCourseImgBase64() {
		return courseImgBase64;
	}

	public void setCourseImgBase64(String courseImgBase64) {
		this.courseImgBase64 = courseImgBase64;
	}

	public byte[] getCourseImg() {
		return courseImg;
	}

	public void setCourseImg(byte[] courseImg) {
		this.courseImg = courseImg;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
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

	@ManyToOne
	@JoinColumn(name = "userid")
	@JsonManagedReference
	private Users users;

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	@OneToMany(mappedBy = "courses", cascade = CascadeType.ALL)
	private List<Purchasedcourse> purchasedcourses;

	public List<Purchasedcourse> getPurchasedcourses() {
		return purchasedcourses;
	}

	public void setPurchasedcourses(List<Purchasedcourse> purchasedcourses) {
		this.purchasedcourses = purchasedcourses;
	}

//
//	@ManyToOne
//	@JoinColumn(name = "userid")
//	@JsonManagedReference
//	private UserInfo userInfo;
//
//	public UserInfo getUserInfo() {
//		return userInfo;
//	}
//
//	public void setUserInfo(UserInfo userInfo) {
//		this.userInfo = userInfo;
//	}

}
