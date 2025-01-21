package org.ezlearn.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CartId implements Serializable {
    private Integer userId;
    private Long courseId;

    // 建構式
    public CartId() {
    }

    public CartId(Integer userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    // Getters
    public Integer getUserId() {
        return userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    // Setters
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
} 