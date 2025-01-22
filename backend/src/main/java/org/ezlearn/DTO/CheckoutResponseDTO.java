package org.ezlearn.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class CheckoutResponseDTO {
    private String orderId;
    private Integer totalAmount;
    private String orderStatus;
    private List<CheckoutItemDTO> items;
    private LocalDateTime createdAt;

    // 無參數建構式
    public CheckoutResponseDTO() {
    }

    // 全參數建構式
    public CheckoutResponseDTO(String orderId, Integer totalAmount, String orderStatus,
                               List<CheckoutItemDTO> items, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.items = items;
        this.createdAt = createdAt;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<CheckoutItemDTO> getItems() {
        return items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setItems(List<CheckoutItemDTO> items) {
        this.items = items;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // 獲取狀態顯示文字
    public String getStatusText() {
        switch (orderStatus) {
            case "COMPLETE": return "已完成";
            case "PENDING": return "待付款";
            case "CANCELLED": return "付款失敗";
            default: return orderStatus;
        }
    }

    // 獲取狀態樣式類
    public String getStatusClass() {
        switch (orderStatus) {
            case "COMPLETE": return "bg-green-100 text-green-800";
            case "PENDING": return "bg-yellow-100 text-yellow-800";
            case "CANCELLED": return "bg-red-100 text-red-800";
            default: return "bg-gray-100 text-gray-800";
        }
    }
} 