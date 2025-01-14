package org.ezlearn.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequest {
    private List<Integer> courseIds;
} 