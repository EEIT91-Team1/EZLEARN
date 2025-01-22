package org.ezlearn.DTO;

import java.util.List;

public class CartResponseDTO {
    private List<CartItemDTO> items;
    private Integer totalPrice;
    private Integer selectedCount;
    private Boolean isAllSelected;
    private List<String> messages;

    // 無參數建構式
    public CartResponseDTO() {
    }

    // 全參數建構式
    public CartResponseDTO(List<CartItemDTO> items, Integer totalPrice, Integer selectedCount,
                           Boolean isAllSelected, List<String> messages) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.selectedCount = selectedCount;
        this.isAllSelected = isAllSelected;
        this.messages = messages;
    }

    // Getters
    public List<CartItemDTO> getItems() {
        return items;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getSelectedCount() {
        return selectedCount;
    }

    public Boolean getIsAllSelected() {
        return isAllSelected;
    }

    public List<String> getMessages() {
        return messages;
    }

    // Setters
    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setSelectedCount(Integer selectedCount) {
        this.selectedCount = selectedCount;
    }

    public void setIsAllSelected(Boolean isAllSelected) {
        this.isAllSelected = isAllSelected;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
} 