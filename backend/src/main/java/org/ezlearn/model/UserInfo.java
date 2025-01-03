package org.ezlearn.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "userinfo")
public class UserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userid")
	private Long userId;
	@Column(name = "username")
	private String userName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUsername(String username) {
		this.userName = username;
	}

	@OneToOne
	@JoinColumn(name = "userid")
	@JsonBackReference
	private Users users;

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

//	@OneToMany(mappedBy = "userInfo")
//	@JsonBackReference
//	private List<Courses> courses;
//
//	public List<Courses> getCourses() {
//		return courses;
//	}
//
//	public void setCourses(List<Courses> courses) {
//		this.courses = courses;
//	}

}
