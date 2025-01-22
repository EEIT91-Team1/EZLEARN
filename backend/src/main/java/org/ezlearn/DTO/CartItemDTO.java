package org.ezlearn.DTO;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItemDTO {
    private Long courseId;
    private String courseName;
    private String courseIntro;
    private String courseImg;
    private Integer price;
    private boolean selected;
    private boolean isPurchased;

    // 無參數建構式
    public CartItemDTO() {
    }

    @JsonCreator
    public CartItemDTO(
            @JsonProperty("courseId") Long courseId,
            @JsonProperty("courseName") String courseName,
            @JsonProperty("courseIntro") String courseIntro,
            @JsonProperty("courseImg") String courseImg,
            @JsonProperty("price") Integer price,
            @JsonProperty("selected") Integer selected,
            @JsonProperty("isPurchased") Integer isPurchased) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseIntro = courseIntro;
        this.courseImg = courseImg;
        this.price = price;
        this.selected = selected != null && selected > 0;
        this.isPurchased = isPurchased != null && isPurchased > 0;
    }

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseIntro() {
        return courseIntro;
    }

    public void setCourseIntro(String courseIntro) {
        this.courseIntro = courseIntro;
    }

    public String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public void setPurchased(boolean isPurchased) {
        this.isPurchased = isPurchased;
    }
} 