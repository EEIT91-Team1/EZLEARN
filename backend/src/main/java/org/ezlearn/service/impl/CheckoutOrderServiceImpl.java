package org.ezlearn.service.impl;

import org.ezlearn.mapper.CheckoutOrderMapper;
import org.ezlearn.model.entity.CheckoutOrder;
import org.ezlearn.model.entity.CheckoutOrderDetail;
import org.ezlearn.model.dto.CheckoutItemDTO;
import org.ezlearn.model.dto.CheckoutResponseDTO;
import org.ezlearn.service.CheckoutOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;

@Service
@Slf4j
public class CheckoutOrderServiceImpl implements CheckoutOrderService {
    
    @Autowired
    private CheckoutOrderMapper checkoutOrderMapper;
    
    @Override
    @Transactional
    public CheckoutResponseDTO createCheckoutOrder(Integer userId, List<Integer> courseIds) {
        log.info("開始創建結帳訂單，userId: {}, courseIds: {}", userId, courseIds);
        
        try {
            // 1. 從資料庫獲取最新價格
            List<CheckoutItemDTO> items = checkoutOrderMapper.getSelectedCourses(courseIds);
            
            // 2. 驗證所有課程是否存在
            if (items.size() != courseIds.size()) {
                log.warn("部分課程不存在，預期數量: {}, 實際數量: {}", courseIds.size(), items.size());
                throw new IllegalArgumentException("部分課程不存在或已下架");
            }
            
            // 3. 計算總金額
            int totalAmount = items.stream()
                    .mapToInt(CheckoutItemDTO::getPrice)
                    .sum();
            
            log.info("訂單總金額: {}", totalAmount);
            
            // 4. 生成訂單編號
            String orderId = generateOrderId();
            
            // 5. 創建訂單
            CheckoutOrder order = new CheckoutOrder();
            order.setOrderId(orderId);
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setOrderStatus("PENDING");
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            
            // 6. 保存訂單和訂單明細
            try {
                checkoutOrderMapper.insertCheckoutOrder(order);
                
                for (CheckoutItemDTO item : items) {
                    CheckoutOrderDetail detail = new CheckoutOrderDetail();
                    detail.setOrderId(orderId);
                    detail.setCourseId(item.getCourseId());
                    detail.setPrice(item.getPrice());
                    detail.setCreatedAt(LocalDateTime.now());
                    
                    checkoutOrderMapper.insertCheckoutOrderDetail(detail);
                }
                
                // 7. 構建響應
                CheckoutResponseDTO response = new CheckoutResponseDTO();
                response.setOrderId(orderId);
                response.setTotalAmount(totalAmount);
                response.setOrderStatus("PENDING");
                response.setItems(items);
                response.setCreatedAt(order.getCreatedAt());
                
                log.info("訂單創建成功，orderId: {}", orderId);
                
                return response;
                
            } catch (Exception e) {
                log.error("保存訂單時發生錯誤: {}", e.getMessage(), e);
                throw new RuntimeException("創建訂單失敗: " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("創建訂單過程中發生錯誤: {}", e.getMessage(), e);
            throw new RuntimeException("創建訂單失敗，請稍後再試");
        }
    }
    
    @Transactional
    private String generateOrderId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int maxAttempts = 5;  // 最大重試次數
        
        for (int i = 0; i < maxAttempts; i++) {
            int sequence = checkoutOrderMapper.getNextOrderSequence();
            String orderId = "ORD" + dateStr + String.format("%04d", sequence);
            
            // 檢查訂單ID是否已存在
            if (checkoutOrderMapper.checkOrderIdExists(orderId) == 0) {
                log.info("生成的訂單編號: {}", orderId);
                return orderId;
            }
            log.warn("訂單編號重複，重試生成: {}", orderId);
        }
        
        throw new RuntimeException("無法生成唯一的訂單編號");
    }
    
    @Override
    public CheckoutResponseDTO getOrderDetails(String orderId) {
        log.info("獲取訂單詳情，orderId: {}", orderId);
        
        try {
            CheckoutOrder order = checkoutOrderMapper.getOrderDetails(orderId);
            if (order == null) {
                log.warn("訂單不存在，orderId: {}", orderId);
                throw new IllegalArgumentException("訂單不存在");
            }
            
            // 移除訂單狀態檢查，直接返回訂單資訊
            List<CheckoutItemDTO> items = order.getItems();
            CheckoutResponseDTO response = buildCheckoutResponse(order, items);
            
            log.info("成功獲取訂單詳情，orderId: {}, status: {}", orderId, order.getOrderStatus());
            return response;
            
        } catch (Exception e) {
            log.error("獲取訂單詳情失敗，orderId: " + orderId, e);
            throw new RuntimeException("獲取訂單詳情失敗: " + e.getMessage());
        }
    }
    
    @Override
    public void updateOrderStatus(String orderId, String status) {
        log.info("更新訂單狀態，orderId: {}, status: {}", orderId, status);
        
        try {
            // 1. 驗證狀態是否有效
            if (!Arrays.asList("PENDING", "COMPLETE", "CANCELLED").contains(status)) {
                throw new IllegalArgumentException("無效的訂單狀態: " + status);
            }
            
            // 2. 獲取當前訂單
            CheckoutOrder order = checkoutOrderMapper.getOrderDetails(orderId);
            if (order == null) {
                throw new IllegalArgumentException("訂單不存在");
            }
            
            // 3. 驗證狀態轉換的合法性
            if (!isValidStatusTransition(order.getOrderStatus(), status)) {
                throw new IllegalArgumentException("無效的狀態轉換");
            }
            
            // 4. 更新訂單狀態
            checkoutOrderMapper.updateOrderStatus(orderId, status);
            
            // 5. 如果是完成狀態，執行額外操作
            if ("COMPLETE".equals(status)) {
                handleOrderCompletion(order);
            }
            
            log.info("訂單狀態更新成功，orderId: {}, newStatus: {}", orderId, status);
            
        } catch (Exception e) {
            log.error("更新訂單狀態失敗，orderId: " + orderId, e);
            throw new RuntimeException("更新訂單狀態失敗: " + e.getMessage());
        }
    }
    
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        // 定義合法的狀態轉換
        switch (currentStatus) {
            case "PENDING":
                return "COMPLETE".equals(newStatus) || "CANCELLED".equals(newStatus);
            case "COMPLETE":
                return false; // 已完成的訂單不能改變狀態
            case "CANCELLED":
                return false; // 已取消的訂單不能改變狀態
            default:
                return false;
        }
    }
    
    private void handleOrderCompletion(CheckoutOrder order) {
        try {
            // TODO: 這裡可以添加將課程加入用戶課程列表的邏輯
            log.info("訂單完成處理成功，orderId: {}", order.getOrderId());
        } catch (Exception e) {
            log.error("處理完成訂單時發生錯誤，orderId: " + order.getOrderId(), e);
            throw new RuntimeException("處理訂單完成狀態失敗", e);
        }
    }
    
    @Override
    public List<CheckoutResponseDTO> getOrderHistory(Integer userId) {
        log.info("獲取用戶訂單歷史，userId: {}", userId);
        return checkoutOrderMapper.getOrderHistory(userId);
    }
    
    private CheckoutResponseDTO buildCheckoutResponse(CheckoutOrder order, List<CheckoutItemDTO> items) {
        CheckoutResponseDTO response = new CheckoutResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setTotalAmount(order.getTotalAmount());
        response.setOrderStatus(order.getOrderStatus());
        response.setItems(items);
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
    
    @Override
    public List<CheckoutItemDTO> getSelectedCourses(List<Integer> courseIds) {
        return checkoutOrderMapper.getSelectedCourses(courseIds);
    }
} 