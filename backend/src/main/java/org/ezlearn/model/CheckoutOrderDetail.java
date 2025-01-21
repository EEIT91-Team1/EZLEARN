package org.ezlearn.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkout_order_details")
@IdClass(CheckoutOrderDetailId.class)
public class CheckoutOrderDetail {
    @Id
    @Column(name = "order_id")
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private CheckoutOrder order;

    @Id
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "price", nullable = false)
    private Integer price;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 建構式
    public CheckoutOrderDetail() {
    }

    public CheckoutOrderDetail(String orderId, CheckoutOrder order, Long courseId, Integer price) {
        this.orderId = orderId;
        this.order = order;
        this.courseId = courseId;
        this.price = price;
    }

    public CheckoutOrderDetail(CheckoutOrder order, Long courseId, Integer price) {
        this.orderId = order.getOrderId();
        this.order = order;
        this.courseId = courseId;
        this.price = price;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public CheckoutOrder getOrder() {
        return order;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Integer getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrder(CheckoutOrder order) {
        this.order = order;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}