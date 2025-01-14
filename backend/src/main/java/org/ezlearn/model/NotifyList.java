package org.ezlearn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class NotifyList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notifyListId;
	@Column(insertable=false, updatable=false)
	private Long notifyId;
	private Long userId;
	private boolean checked;
	private String createdAt;
	
	
	
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(Long notifyId) {
		this.notifyId = notifyId;
	}
	public Long getNotifyListId() {
		return notifyListId;
	}
	public void setNotifyListId(Long notifyListId) {
		this.notifyListId = notifyListId;
	}
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	
	@ManyToOne
	@JoinColumn(name="notifyId")
	private Notify notify;

	public Notify getNotify() {
		return notify;
	}
	public void setNotify(Notify notify) {
		this.notify = notify;
	}
	
}