package org.ezlearn.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
public class CheckoutItemDTO {
    private Integer courseId;         // 課程ID
    private String courseName;        // 課程名稱
    private String courseIntro;       // 課程簡介
    private String courseImg;         // 課程圖片
    private Integer price;            // 價格
    private Boolean isSelected;       // 是否被選中（購物車使用）

    @JsonCreator
    public CheckoutItemDTO(
            @JsonProperty("courseId") Integer courseId,
            @JsonProperty("courseName") String courseName,
            @JsonProperty("courseIntro") String courseIntro,
            @JsonProperty("courseImg") byte[] courseImg,
            @JsonProperty("price") Integer price) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseIntro = courseIntro;
        this.courseImg = courseImg != null ? Base64.getEncoder().encodeToString(courseImg) : null;
        this.price = price;
        this.isSelected = false;
    }
} 