package org.ezlearn.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;

@Entity
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	private String email;
	private String password;
	@Transient
	private String comfirmpassword;
	public String getComfirmpassword() {
		return comfirmpassword;
	}
	public void setComfirmpassword(String comfirmpassword) {
		this.comfirmpassword = comfirmpassword;
	}
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@OneToMany(mappedBy = "users")
	private List<PurchasedCourses> purchasedCourses;
	
	
	
	public List<PurchasedCourses> getPurchasedCourses() {
		return purchasedCourses;
	}
	public void setPurchasedCourses(List<PurchasedCourses> purchasedCourses) {
		this.purchasedCourses = purchasedCourses;
	}

	//---------------------
	@OneToOne(mappedBy = "users",cascade = CascadeType.ALL)
	private UserInfo userinfo;
	public UserInfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
		this.userinfo.setUser(this);
	}
	
	@OneToMany(mappedBy="users")
	private List<WishList> wishList;
	public List<WishList> getWishList() {
		return wishList;
	}
	public void setWishList(List<WishList> wishList) {
		this.wishList = wishList;
	}

	@OneToMany(mappedBy="users")
	private List<Courses> courses;
	public List<Courses> getCourses() {
		return courses;
	}
	public void setCourses(List<Courses> courses) {
		this.courses = courses;
	}
	
}
