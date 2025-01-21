package org.ezlearn.DTO;

public class OrderItemDTO {
    private Integer courseId;     // 課程ID
    private String courseName;    // 課程名稱
    private Integer price;        // 價格

    // 無參數建構式
    public OrderItemDTO() {
    }

    // 全參數建構式
    public OrderItemDTO(Integer courseId, String courseName, Integer price) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.price = price;
    }

    // Getters
    public Integer getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public Integer getPrice() {
        return price;
    }

    // Setters
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
} 