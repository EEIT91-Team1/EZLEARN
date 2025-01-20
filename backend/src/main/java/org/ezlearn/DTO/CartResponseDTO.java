package org.ezlearn.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CartResponseDTO {
    private List<CartItemDTO> items;
    private Integer totalPrice;
    private Integer selectedCount;
    private Boolean isAllSelected;
    private List<String> messages;
} 