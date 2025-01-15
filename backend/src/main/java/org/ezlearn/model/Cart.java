package org.ezlearn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Integer userId;
    private Integer courseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 