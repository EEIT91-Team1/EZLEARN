package org.ezlearn.model.dto;

import lombok.Data;

@Data
public class CheckoutItemDTO {
    private Integer courseId;         // 課程ID
    private String courseName;        // 課程名稱
    private String courseIntro;       // 課程簡介
    private String courseImg;         // 課程圖片
    private Integer price;            // 價格
    private Boolean isSelected;       // 是否被選中（購物車使用）
} 