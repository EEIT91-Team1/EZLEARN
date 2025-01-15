package org.ezlearn.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CheckoutOrderDetail {
    private String orderId;
    private Integer courseId;
    private Integer price;
    private LocalDateTime createdAt;
}