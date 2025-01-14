package org.ezlearn.service;

import org.ezlearn.model.dto.CartResponseDTO;
import java.util.List;

public interface CartService {
    CartResponseDTO getCartItems(Integer userId);
    CartResponseDTO removeFromCart(Integer userId, Integer courseId);
    boolean addToCart(Integer userId, Integer courseId);
    int calculateTotal(List<Integer> courseIds);
    void removeCheckedOutCourses(Integer userId, List<Integer> courseIds);
} 