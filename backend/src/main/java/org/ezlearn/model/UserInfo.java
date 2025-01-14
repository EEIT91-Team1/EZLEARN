package org.ezlearn.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_info")
public class UserInfo {
	
	@Id
	private Long userId;
	private String gender;
	private String birthday;
	private byte[] avatar;
	private String phone;
	private String userName;
	private String userIntro;
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public byte[] getAvatar() {
		return avatar;
	}
	
	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserIntro() {
		return userIntro;
	}
	
	public void setUserIntro(String userIntro) {
		this.userIntro = userIntro;
	}
	
	@OneToOne
	@MapsId
	@JoinColumn(name = "user_id")
	private Users users;
	
	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

}
