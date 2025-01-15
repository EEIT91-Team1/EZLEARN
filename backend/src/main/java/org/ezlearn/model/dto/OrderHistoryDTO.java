package org.ezlearn.model.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderHistoryDTO {
    private String orderId;
    private Integer totalAmount;
    private String orderStatus;
    private LocalDateTime createdAt;
    private List<CheckoutItemDTO> items;
    
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