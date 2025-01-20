package org.ezlearn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutOrderDetailId implements Serializable {
    private String orderId;
    private Integer courseId;
} 