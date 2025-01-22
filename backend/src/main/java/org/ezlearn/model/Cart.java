package org.ezlearn.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "carts")
public class Cart {
    @EmbeddedId
    private CartId cartId;

    public CartId getCartId(){
        return cartId;
    }
    public void setCartId(CartId cartId){
        this.cartId=cartId;
    }

    @ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private Users users;
	
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	
	@ManyToOne
	@MapsId("courseId")
	@JoinColumn(name = "course_id")
	private Courses courses;

	public Courses getCourses() {
		return courses;
	}
	public void setCourses(Courses courses) {
		this.courses = courses;
	}


    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 建構式
    public Cart() {
    }

    public Cart(Long userId, Long courseId, Users user, Courses course) {
    	this.cartId = new CartId(userId, courseId);
        this.users = user;
        this.courses = course;
    }




    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 