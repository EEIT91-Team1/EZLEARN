package org.ezlearn.model.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import org.ezlearn.model.dto.CheckoutItemDTO;

@Data
public class CheckoutOrder {
    private String orderId;
    private Integer userId;
    private Integer totalAmount;
    private String orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CheckoutItemDTO> items;
}