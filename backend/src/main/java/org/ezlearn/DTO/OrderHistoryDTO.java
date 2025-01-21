package org.ezlearn.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class OrderHistoryDTO {
    private String orderId;
    private Integer totalAmount;
    private String orderStatus;
    private LocalDateTime createdAt;
    private List<CheckoutItemDTO> items;

    // 無參數建構式
    public OrderHistoryDTO() {
    }

    // 全參數建構式
    public OrderHistoryDTO(String orderId, Integer totalAmount, String orderStatus,
                           LocalDateTime createdAt, List<CheckoutItemDTO> items) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.items = items;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<CheckoutItemDTO> getItems() {
        return items;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setItems(List<CheckoutItemDTO> items) {
        this.items = items;
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