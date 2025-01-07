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
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Courses {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseId;
	@Column(insertable = false, updatable = false)
	private Long teacherId;
	private Integer price;
	private String courseName;
	private String courseIntro;
	private String courseType;
	private byte[] courseImg;

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

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

	@OneToMany(mappedBy = "courses")
	private List<PurchasedCourses> purchasedCourses;
	
	
	public List<PurchasedCourses> getPurchasedCourses() {
		return purchasedCourses;
	}

	public void setPurchasedCourses(List<PurchasedCourses> purchasedCourses) {
		this.purchasedCourses = purchasedCourses;
	}

	@OneToMany(mappedBy = "courses")
	private List<WishList> wishList;

	public List<WishList> getWishList() {
		return wishList;
	}

	public void setWishList(List<WishList> wishList) {
		this.wishList = wishList;
	}

	@ManyToOne
	@JoinColumn(name = "teacherId")
	private Users users;

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}
	
	
}
