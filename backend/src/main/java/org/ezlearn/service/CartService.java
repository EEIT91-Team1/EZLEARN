package org.ezlearn.service;

import org.ezlearn.DTO.CartItemDTO;
import org.ezlearn.DTO.CartResponseDTO;
import org.ezlearn.repository.CartRepository;
import org.ezlearn.repository.CoursesRepository;
import org.ezlearn.model.Cart;
import org.ezlearn.model.Courses;
import org.ezlearn.model.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import jakarta.servlet.http.HttpSession;

@Service
public class CartService {
    
    @Autowired
    private HttpSession httpSession;

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private CartRepository cartRepository;
    
    @Transactional
    public CartResponseDTO getCartItems(Long userId) {

        List<CartItemDTO> items = cartRepository.findCartItemsByUserId(userId);
        
        List<CartItemDTO> filteredItems = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        
        for (CartItemDTO item : items) {
            if (item.getCourseImg() != null) {
                item.setCourseImg("data:image/*;base64," + item.getCourseImg());
            }
            
            if (item.isPurchased()) {
                // 自動從購物車中移除已購買的課程
                cartRepository.deleteByUserIdAndCourseId(userId, item.getCourseId());
                messages.add(item.getCourseName() + " 已購買，已自動從購物車中移除");
            } else {
                filteredItems.add(item);
            }
        }
        
        CartResponseDTO response = buildCartResponse(filteredItems);
        response.setMessages(messages);
        
        return response;
    }
    
    public CartResponseDTO removeFromCart(Long userId, Long courseId) {

        int result = cartRepository.deleteByUserIdAndCourseId(userId, courseId);
        
        return getCartItems(userId);
    }
    
    public boolean addToCart(Long userId, Long courseId) {
        Users user = (Users) httpSession.getAttribute("user");

        // 檢查是否已存在
        if (cartRepository.existsByUserIdAndCourseId(user.getUserId(), courseId)) {
            return false;
        }
        
        // 創建並保存購物車項目
        Courses course = coursesRepository.findByCourseId(courseId); 
        Cart cart = new Cart(userId, courseId, user, course);
        
        cart.setUsers(user);
        cart.setCourses(course);
        
        try {
            cartRepository.save(cart);
            return true;
        } catch (Exception e) {
        	System.out.println(e);
            return false;
        }
    }
    
    @Transactional(readOnly = true)
    public int calculateTotal(List<Long> courseIds) {
        
        if (courseIds == null || courseIds.isEmpty()) {
            return 0;
        }
        
        try {
            // 從資料庫獲取最新的課程價格進行驗證
            List<CartItemDTO> items = cartRepository.findSelectedCourses(courseIds);
            
            // 驗證所有課程是否存在且價格正確
            if (items.size() != courseIds.size()) {
                throw new IllegalArgumentException("部分課程不存在或已下架");
            }
            
            // 計算總價
            int totalAmount = items.stream()
                    .mapToInt(CartItemDTO::getPrice)
                    .sum();
                    
            return totalAmount;
            
        } catch (Exception e) {
            throw new RuntimeException("計算總價失敗", e);
        }
    }
    
    public void removeCheckedOutCourses(Long userId, List<Long> courseIds) {
        
        if (userId == null || courseIds == null || courseIds.isEmpty()) {
            throw new IllegalArgumentException("無效的參數");
        }
        
        try {
            // 先驗證課程價格
            List<CartItemDTO> items = cartRepository.findSelectedCourses(courseIds);
            if (items.size() != courseIds.size()) {
                throw new IllegalArgumentException("部分課程不存在或已下架");
            }
            
            // 移除購物車中的課程
            int removedCount = cartRepository.deleteCheckedOutCourses(userId, courseIds);
            
            if (removedCount != courseIds.size()) {
                throw new RuntimeException("部分課程移除失敗");
            }
        } catch (Exception e) {
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