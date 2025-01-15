package org.ezlearn.service;

import org.ezlearn.model.dto.CheckoutResponseDTO;
import org.ezlearn.model.dto.CheckoutItemDTO;
import java.util.List;

public interface CheckoutOrderService {
    CheckoutResponseDTO createCheckoutOrder(Integer userId, List<Integer> courseIds);
    CheckoutResponseDTO getOrderDetails(String orderId);
    void updateOrderStatus(String orderId, String status);
    List<CheckoutResponseDTO> getOrderHistory(Integer userId);
    List<CheckoutItemDTO> getSelectedCourses(List<Integer> courseIds);
} 