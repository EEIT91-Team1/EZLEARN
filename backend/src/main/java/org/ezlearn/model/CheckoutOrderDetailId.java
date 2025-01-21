package org.ezlearn.model;

import java.io.Serializable;

public class CheckoutOrderDetailId implements Serializable {
    private String orderId;
    private Long courseId;

    // 建構式
    public CheckoutOrderDetailId() {
    }

    public CheckoutOrderDetailId(String orderId, Long courseId) {
        this.orderId = orderId;
        this.courseId = courseId;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public Long getCourseId() {
        return courseId;
    }

    // Setters
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
} 