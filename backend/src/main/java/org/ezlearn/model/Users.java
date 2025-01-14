package org.ezlearn.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class Users {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	private String email;
	private String password;
	private String resetToken;
	private String resetTokenExpiry;

	
	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public String getResetTokenExpiry() {
		return resetTokenExpiry;
	}

	public void setResetTokenExpiry(String resetTokenExpiry) {
		this.resetTokenExpiry = resetTokenExpiry;
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
	
	@OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private UserInfo userInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		this.userInfo.setUsers(this);
	}
	
	@OneToMany(mappedBy="users")
	private List<WishList> wishList;
	public List<WishList> getWishList() {
		return wishList;
	}
	public void setWishList(List<WishList> wishList) {
		this.wishList = wishList;
	}

	
}
