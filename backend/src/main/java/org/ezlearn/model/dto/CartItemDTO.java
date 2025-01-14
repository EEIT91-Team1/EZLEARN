package org.ezlearn.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartItemDTO {
    private Integer courseId;
    private String courseName;
    private String courseIntro;
    private String courseImg;
    private Integer price;
    private Boolean isSelected;
    private LocalDateTime createdAt;
} 