package org.ezlearn.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Base64;

public class CheckoutItemDTO {
    private Long courseId;         // 課程ID
    private String courseName;        // 課程名稱
    private String courseIntro;       // 課程簡介
    private String courseImg;         // 課程圖片
    private Integer price;            // 價格
    private Boolean isSelected;       // 是否被選中（購物車使用）

    // 無參數建構式
    public CheckoutItemDTO() {
    }

    // JsonCreator 建構式
    @JsonCreator
    public CheckoutItemDTO(
            @JsonProperty("courseId") Long courseId,
            @JsonProperty("courseName") String courseName,
            @JsonProperty("courseIntro") String courseIntro,
            @JsonProperty("courseImg") String courseImg,
            @JsonProperty("price") Integer price,
            @JsonProperty("isSelected") Boolean isSelected) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseIntro = courseIntro;
        this.courseImg = courseImg;
        this.price = price;
        this.isSelected = isSelected;
    }

    // 用於 native query 的建構函數
    public CheckoutItemDTO(Long courseId, 
                         String courseName, 
                         String courseIntro,
                         String courseImg, 
                         Integer price) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseIntro = courseIntro;
        this.courseImg = courseImg;
        this.price = price;
        this.isSelected = false;  // 預設值
    }

    // Getters
    public Long getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseIntro() {
        return courseIntro;
    }

    public String getCourseImg() {
        return courseImg;
    }

    public Integer getPrice() {
        return price;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    // Setters
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseIntro(String courseIntro) {
        this.courseIntro = courseIntro;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }
} 