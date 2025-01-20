package org.ezlearn.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkout_order_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private Integer courseId;

    @Column(name = "price", nullable = false)
    private Integer price;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 建構子
    public CheckoutOrderDetail(CheckoutOrder order, Integer courseId, Integer price) {
        this.orderId = order.getOrderId();
        this.order = order;
        this.courseId = courseId;
        this.price = price;
    }
}