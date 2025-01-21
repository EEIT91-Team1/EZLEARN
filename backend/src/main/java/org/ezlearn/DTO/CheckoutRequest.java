package org.ezlearn.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CheckoutRequest {

    @NotNull(message = "課程ID列表不能為空")
    @NotEmpty(message = "請選擇至少一個課程")
    private List<Long> courseIds;

    // 無參數建構式
    public CheckoutRequest() {
    }

    // 全參數建構式
    public CheckoutRequest(List<Long> courseIds) {
        this.courseIds = courseIds;
    }

    // Getter
    public List<Long> getCourseIds() {
        return courseIds;
    }

    // Setter
    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }
} 