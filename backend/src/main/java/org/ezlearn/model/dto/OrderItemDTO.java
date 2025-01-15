package org.ezlearn.model.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Integer courseId;     // 課程ID
    private String courseName;    // 課程名稱
    private Integer price;        // 價格
} 