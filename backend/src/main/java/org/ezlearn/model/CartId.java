package org.ezlearn.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CartId implements Serializable {
    private Long userId;
    private Long courseId;

    // 建構式
    public CartId() {
    }

    public CartId(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    // Getters
    public Long getUserId() {
        return userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    // Setters
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
} 