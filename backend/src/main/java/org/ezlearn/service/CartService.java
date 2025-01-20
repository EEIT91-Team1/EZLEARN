package org.ezlearn.service;

import org.ezlearn.DTO.CartItemDTO;
import org.ezlearn.DTO.CartResponseDTO;
import org.ezlearn.repository.CartRepository;
import org.ezlearn.model.Cart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Transactional
    public CartResponseDTO getCartItems(Integer userId) {
        log.info("獲取用戶購物車內容 - userId: {}", userId);
        
        List<CartItemDTO> items = cartRepository.findCartItemsByUserId(userId);
        
        // 處理每個項目的圖片並移除已購買的課程
        List<CartItemDTO> filteredItems = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        
        for (CartItemDTO item : items) {
            if (item.getCourseImg() != null) {
                item.setCourseImg("data:image/*;base64," + item.getCourseImg());
            }
            
            if (item.isPurchased()) {
                // 自動從購物車中移除已購買的課程
                cartRepository.deleteByUserIdAndCourseId(userId, item.getCourseId().intValue());
                messages.add(item.getCourseName() + " 已購買，已自動從購物車中移除");
                log.info("已購買的課程已從購物車移除 - userId: {}, courseId: {}", userId, item.getCourseId());
            } else {
                filteredItems.add(item);
            }
        }
        
        CartResponseDTO response = buildCartResponse(filteredItems);
        response.setMessages(messages);
        
        return response;
    }
    
    public CartResponseDTO removeFromCart(Integer userId, Integer courseId) {
        log.info("從購物車移除課程 - userId: {}, courseId: {}", userId, courseId);
        
        int result = cartRepository.deleteByUserIdAndCourseId(userId, courseId);
        if (result <= 0) {
            log.warn("課程不存在於購物車中 - userId: {}, courseId: {}", userId, courseId);
        }
        
        return getCartItems(userId);
    }
    
    public boolean addToCart(Integer userId, Integer courseId) {
        log.info("添加課程到購物車 - userId: {}, courseId: {}", userId, courseId);
        
        // 檢查是否已存在
        if (cartRepository.existsByUserIdAndCourseId(userId, courseId)) {
            log.info("課程已存在於購物車中 - userId: {}, courseId: {}", userId, courseId);
            return false;
        }
        
        // 創建並保存購物車項目
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCourseId(courseId);
        
        try {
            cartRepository.save(cart);
            return true;
        } catch (Exception e) {
            log.error("添加課程到購物車失敗 - userId: {}, courseId: {}", userId, courseId, e);
            return false;
        }
    }
    
    @Transactional(readOnly = true)
    public int calculateTotal(List<Integer> courseIds) {
        log.info("計算課程總價 - courseIds: {}", courseIds);
        
        if (courseIds == null || courseIds.isEmpty()) {
            return 0;
        }
        
        try {
            // 從資料庫獲取最新的課程價格進行驗證
            List<CartItemDTO> items = cartRepository.findSelectedCourses(courseIds);
            
            // 驗證所有課程是否存在且價格正確
            if (items.size() != courseIds.size()) {
                log.warn("部分課程不存在或已下架 - courseIds: {}", courseIds);
                throw new IllegalArgumentException("部分課程不存在或已下架");
            }
            
            // 計算總價
            int totalAmount = items.stream()
                    .mapToInt(CartItemDTO::getPrice)
                    .sum();
                    
            log.info("計算總價完成 - courseIds: {}, totalAmount: {}", courseIds, totalAmount);
            return totalAmount;
            
        } catch (Exception e) {
            log.error("計算總價失敗 - courseIds: {}", courseIds, e);
            throw new RuntimeException("計算總價失敗", e);
        }
    }
    
    public void removeCheckedOutCourses(Integer userId, List<Integer> courseIds) {
        log.info("移除已結帳課程 - userId: {}, courseIds: {}", userId, courseIds);
        
        if (userId == null || courseIds == null || courseIds.isEmpty()) {
            log.warn("無效的參數 - userId: {}, courseIds: {}", userId, courseIds);
            throw new IllegalArgumentException("無效的參數");
        }
        
        try {
            // 先驗證課程價格
            List<CartItemDTO> items = cartRepository.findSelectedCourses(courseIds);
            if (items.size() != courseIds.size()) {
                log.warn("部分課程不存在或已下架 - courseIds: {}", courseIds);
                throw new IllegalArgumentException("部分課程不存在或已下架");
            }
            
            // 移除購物車中的課程
            int removedCount = cartRepository.deleteCheckedOutCourses(userId, courseIds);
            log.info("成功移除課程 - userId: {}, removedCount: {}", userId, removedCount);
            
            if (removedCount != courseIds.size()) {
                log.warn("部分課程移除失敗 - 預期: {}, 實際: {}", courseIds.size(), removedCount);
                throw new RuntimeException("部分課程移除失敗");
            }
        } catch (Exception e) {
            log.error("移除已結帳課程失敗 - userId: {}, courseIds: {}", userId, courseIds, e);
            throw new RuntimeException("移除已結帳課程失敗", e);
        }
    }
    
    // 抽取共用邏輯：構建購物車回應
    private CartResponseDTO buildCartResponse(List<CartItemDTO> items) {
        CartResponseDTO response = new CartResponseDTO();
        response.setItems(items);
        
        // 計算總價和選中數量
        int totalPrice = 0;
        int selectedCount = 0;
        boolean allSelected = true;
        
        for (CartItemDTO item : items) {
            if (item.isSelected()) {
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
} 