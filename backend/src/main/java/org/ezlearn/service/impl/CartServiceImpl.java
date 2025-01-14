package org.ezlearn.service.impl;

import org.ezlearn.mapper.CartMapper;
import org.ezlearn.model.dto.CartItemDTO;
import org.ezlearn.model.dto.CartResponseDTO;
import org.ezlearn.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartMapper cartMapper;
    
    private String convertBlobToBase64(byte[] blob) {
        if (blob == null || blob.length == 0) {
            return "";
        }
        return Base64.getEncoder().encodeToString(blob);
    }
    
    @Override
    @Transactional
    public CartResponseDTO removeFromCart(Integer userId, Integer courseId) {
        log.info("Removing course {} from cart for user {}", courseId, userId);
        
        // 執行刪除操作
        int result = cartMapper.removeFromCart(userId, courseId);
        if (result <= 0) {
            log.warn("Course {} not found in cart for user {}", courseId, userId);
        }
        
        // 重新獲取購物車內容
        return getCartItems(userId);
    }
    
    @Override
    public CartResponseDTO getCartItems(Integer userId) {
        List<CartItemDTO> items = cartMapper.getCartItems(userId);
        
        // 處理每個項目的圖片
        for (CartItemDTO item : items) {
            if (item.getCourseImg() != null) {
                item.setCourseImg("data:image/jpeg;base64," + item.getCourseImg());
            }
        }
        
        CartResponseDTO response = new CartResponseDTO();
        response.setItems(items);
        
        // 計算總價和選中數量
        int totalPrice = 0;
        int selectedCount = 0;
        boolean allSelected = true;
        
        for (CartItemDTO item : items) {
            if (item.getIsSelected()) {
                totalPrice += item.getPrice();
                selectedCount++;
            } else {
                allSelected = false;
            }
        }
        
        response.setTotalPrice(totalPrice);
        response.setSelectedCount(selectedCount);
        response.setIsAllSelected(allSelected && !items.isEmpty());
        
        return response;
    }
    
    @Override
    public boolean addToCart(Integer userId, Integer courseId) {
        if (cartMapper.checkItemExists(userId, courseId) > 0) {
            return false;
        }
        return cartMapper.addToCart(userId, courseId) > 0;
    }
    
    @Override
    @Transactional(readOnly = true)
    public int calculateTotal(List<Integer> courseIds) {
        try {
            if (courseIds == null || courseIds.isEmpty()) {
                return 0;
            }
            
            List<CartItemDTO> items = cartMapper.getSelectedCourses(courseIds);
            
            // 驗證所有課程是否存在
            if (items.size() != courseIds.size()) {
                throw new IllegalArgumentException("部分課程不存在或已下架");
            }
            
            // 計算總價
            return items.stream()
                    .mapToInt(CartItemDTO::getPrice)
                    .sum();
        } catch (Exception e) {
            log.error("計算總價失敗: courseIds = {}", courseIds, e);
            throw new RuntimeException("計算總價失敗", e);
        }
    }

    @Override
    @Transactional
    public void removeCheckedOutCourses(Integer userId, List<Integer> courseIds) {
        try {
            if (userId == null || courseIds == null || courseIds.isEmpty()) {
                log.warn("無效的參數 - userId: {}, courseIds: {}", userId, courseIds);
                throw new IllegalArgumentException("無效的參數");
            }
            
            log.info("開始從購物車移除已購買課程 - userId: {}, courseIds: {}", userId, courseIds);
            int removedCount = cartMapper.removeCheckedOutCourses(userId, courseIds);
            log.info("成功從購物車移除 {} 個課程 - userId: {}", removedCount, userId);
            
            if (removedCount != courseIds.size()) {
                log.warn("部分課程移除失敗 - 預期: {}, 實際: {}", courseIds.size(), removedCount);
                throw new RuntimeException("部分課程移除失敗");
            }
        } catch (Exception e) {
            log.error("從購物車移除已購買課程失敗 - userId: {}, courseIds: {}", userId, courseIds, e);
            throw new RuntimeException("從購物車移除已購買課程失敗", e);
        }
    }
} 