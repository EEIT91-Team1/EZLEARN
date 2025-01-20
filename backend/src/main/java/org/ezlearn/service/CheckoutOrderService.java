package org.ezlearn.service;

import org.ezlearn.DTO.CheckoutItemDTO;
import org.ezlearn.DTO.CheckoutResponseDTO;
import org.ezlearn.model.CheckoutOrder;
import org.ezlearn.model.CheckoutOrderDetail;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.WishListId;
import org.ezlearn.repository.CheckoutOrderRepository;
import org.ezlearn.repository.CheckoutOrderDetailRepository;
import org.ezlearn.repository.PurchasedCoursesRepository;
import org.ezlearn.repository.WishListRepository;
import org.ezlearn.repository.CoursesRepository;
import org.ezlearn.model.CheckoutOrder.OrderStatus;
import org.ezlearn.model.PurchasedCoursesId;
import org.ezlearn.repository.CartRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Date;

@Service
@Slf4j
public class CheckoutOrderService {
    
    @Autowired
    private CheckoutOrderRepository checkoutOrderRepository;
    
    @Autowired
    private CheckoutOrderDetailRepository checkoutOrderDetailRepository;
    
    @Autowired
    private PurchasedCoursesRepository purchasedCoursesRepository;
    
    @Autowired
    private WishListRepository wishListRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @PostConstruct
    public void initializeOrderStatus() {
        log.info("開始檢查所有訂單狀態...");
        try {
            // 獲取所有待處理的訂單
            List<CheckoutOrder> pendingOrders = checkoutOrderRepository.findByOrderStatus("PENDING");
            
            for (CheckoutOrder order : pendingOrders) {
                // 檢查是否有相同商品的已付款訂單
                Long count = checkoutOrderRepository.countPaidOrderWithSameCourses(order.getUserId(), order.getOrderId());
                if (count != null && count > 0) {
                    // 如果存在相同商品的已付款訂單，將當前訂單標記為取消
                    order.setOrderStatus(OrderStatus.CANCELLED);
                    order.setUpdatedAt(LocalDateTime.now());
                    checkoutOrderRepository.save(order);
                    log.info("訂單已標記為取消（存在相同商品的已付款訂單）- orderId: {}", order.getOrderId());
                }
            }
            log.info("訂單狀態檢查完成");
        } catch (Exception e) {
            log.error("初始化訂單狀態時發生錯誤", e);
        }
    }
    
    @Transactional
    public CheckoutResponseDTO createCheckoutOrder(Integer userId, List<Integer> courseIds) {
        log.info("開始創建結帳訂單，userId: {}, courseIds: {}", userId, courseIds);
        
        try {
            // 1. 從資料庫獲取最新價格
            List<CheckoutItemDTO> items = checkoutOrderRepository.findSelectedCourses(courseIds);
            
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
            order.setOrderStatus(OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());
            
            // 6. 保存訂單和訂單明細
            checkoutOrderRepository.save(order);
            
            for (CheckoutItemDTO item : items) {
                CheckoutOrderDetail detail = new CheckoutOrderDetail(
                    order,  // 直接傳入 order 對象
                    item.getCourseId(),
                    item.getPrice()
                );
                checkoutOrderDetailRepository.save(detail);
            }
            
            // 7. 構建響應
            CheckoutResponseDTO response = new CheckoutResponseDTO();
            response.setOrderId(orderId);
            response.setTotalAmount(totalAmount);
            response.setOrderStatus(order.getOrderStatus().toString());
            response.setItems(items);
            response.setCreatedAt(order.getCreatedAt());
            
            log.info("訂單創建成功，orderId: {}", orderId);
            
            return response;
            
        } catch (Exception e) {
            log.error("創建訂單過程中發生錯誤: {}", e.getMessage(), e);
            throw new RuntimeException("創建訂單失敗，請稍後再試");
        }
    }
    
    @Transactional
    private String generateOrderId() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int maxAttempts = 5;
        
        for (int i = 0; i < maxAttempts; i++) {
            int sequence = checkoutOrderRepository.getNextOrderSequence();
            String orderId = "ORD" + dateStr + String.format("%04d", sequence);
            
            if (!checkoutOrderRepository.existsByOrderId(orderId)) {
                log.info("生成的訂單編號: {}", orderId);
                return orderId;
            }
            log.warn("訂單編號重複，重試生成: {}", orderId);
        }
        
        throw new RuntimeException("無法生成唯一的訂單編號");
    }
    
    public CheckoutResponseDTO getOrderDetails(String orderId) {
        log.info("獲取訂單詳情，orderId: {}", orderId);
        
        try {
            CheckoutOrder order = checkoutOrderRepository.findOrderWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("訂單不存在"));
            
            // 獲取所有課程ID
            List<Integer> courseIds = order.getItems().stream()
                .map(CheckoutOrderDetail::getCourseId)
                .collect(Collectors.toList());
            
            // 獲取課程詳細信息
            List<CheckoutItemDTO> items = checkoutOrderRepository.findSelectedCourses(courseIds);
            
            // 將價格信息從訂單明細中補充到課程信息中
            Map<Integer, Integer> coursePriceMap = order.getItems().stream()
                .collect(Collectors.toMap(
                    CheckoutOrderDetail::getCourseId,
                    CheckoutOrderDetail::getPrice
                ));
            
            items.forEach(item -> item.setPrice(coursePriceMap.get(item.getCourseId())));
            
            CheckoutResponseDTO response = buildCheckoutResponse(order, items);
            
            log.info("成功獲取訂單詳情，orderId: {}, status: {}", orderId, order.getOrderStatus());
            return response;
            
        } catch (Exception e) {
            log.error("獲取訂單詳情失敗，orderId: " + orderId, e);
            throw new RuntimeException("獲取訂單詳情失敗: " + e.getMessage());
        }
    }
    
    @Transactional
    public void updateOrderStatus(String orderId, String status) {
        log.info("更新訂單狀態 - orderId: {}, status: {}", orderId, status);
        
        CheckoutOrder order = checkoutOrderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("訂單不存在"));
        
        // 檢查是否有相同商品的已付款訂單
        if (status.equals("COMPLETE")) {
            Long count = checkoutOrderRepository.countPaidOrderWithSameCourses(order.getUserId(), orderId);
            if (count != null && count > 0) {
                // 如果存在相同商品的已付款訂單，將當前訂單標記為付款失敗
                order.setOrderStatus(OrderStatus.CANCELLED);
                order.setUpdatedAt(LocalDateTime.now());
                checkoutOrderRepository.save(order);
                log.info("訂單已標記為付款失敗（存在相同商品的已付款訂單）- orderId: {}", orderId);
                return;
            }
        }
        
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        order.setOrderStatus(orderStatus);
        order.setUpdatedAt(LocalDateTime.now());
        
        if (orderStatus == OrderStatus.COMPLETE) {
            // 處理已付款訂單
            List<Integer> courseIds = order.getItems().stream()
                .map(CheckoutOrderDetail::getCourseId)
                .collect(Collectors.toList());
                
            // 添加到已購買課程
            for (Integer courseId : courseIds) {
                try {
                    checkoutOrderRepository.insertPurchasedCourse(order.getUserId(), courseId);
                    
                    // 從願望清單中移除
                    try {
                        wishListRepository.deleteWish(order.getUserId().toString(), courseId.toString());
                        log.info("課程已從願望清單中移除 - userId: {}, courseId: {}", 
                                order.getUserId(), courseId);
                    } catch (Exception e) {
                        log.warn("從願望清單移除課程失敗 - userId: {}, courseId: {}", 
                                order.getUserId(), courseId, e);
                    }
                } catch (Exception e) {
                    log.error("添加已購買課程失敗 - userId: {}, courseId: {}", 
                             order.getUserId(), courseId, e);
                }
            }
            
            // 從購物車中移除
            cartRepository.deleteCheckedOutCourses(order.getUserId(), courseIds);
        }
        
        checkoutOrderRepository.save(order);
        log.info("訂單狀態已更新 - orderId: {}, status: {}", orderId, status);
    }
    
    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                return newStatus == OrderStatus.COMPLETE || newStatus == OrderStatus.CANCELLED;
            case COMPLETE:
                return false; // 已完成的訂單不能改變狀態
            case CANCELLED:
                return false; // 已取消的訂單不能改變狀態
            default:
                return false;
        }
    }
    
    public List<CheckoutResponseDTO> getOrderHistory(Integer userId) {
        log.info("獲取用戶訂單歷史，userId: {}", userId);
        
        // 先獲取所有訂單
        List<CheckoutOrder> orders = checkoutOrderRepository.findOrderHistoryByUserId(userId);
        
        // 檢查並更新待處理訂單的狀態
        for (CheckoutOrder order : orders) {
            if ("PENDING".equals(order.getOrderStatus().toString())) {
                // 檢查是否有相同商品的已付款訂單
                Long count = checkoutOrderRepository.countPaidOrderWithSameCourses(order.getUserId(), order.getOrderId());
                if (count != null && count > 0) {
                    // 如果存在相同商品的已付款訂單，將當前訂單標記為取消
                    order.setOrderStatus(OrderStatus.CANCELLED);
                    order.setUpdatedAt(LocalDateTime.now());
                    checkoutOrderRepository.save(order);
                    log.info("訂單已標記為取消（存在相同商品的已付款訂單）- orderId: {}", order.getOrderId());
                }
            }
        }
        
        // 重新獲取更新後的訂單列表
        orders = checkoutOrderRepository.findOrderHistoryByUserId(userId);
        
        return orders.stream()
            .map(order -> {
                // 獲取所有課程ID
                List<Integer> courseIds = order.getItems().stream()
                    .map(CheckoutOrderDetail::getCourseId)
                    .collect(Collectors.toList());
                
                // 獲取課程詳細信息
                List<CheckoutItemDTO> items = checkoutOrderRepository.findSelectedCourses(courseIds);
                
                // 將價格信息從訂單明細中補充到課程信息中
                Map<Integer, Integer> coursePriceMap = order.getItems().stream()
                    .collect(Collectors.toMap(
                        CheckoutOrderDetail::getCourseId,
                        CheckoutOrderDetail::getPrice
                    ));
                
                items.forEach(item -> item.setPrice(coursePriceMap.get(item.getCourseId())));
                
                return buildCheckoutResponse(order, items);
            })
            .collect(Collectors.toList());
    }
    
    private CheckoutResponseDTO buildCheckoutResponse(CheckoutOrder order, List<CheckoutItemDTO> items) {
        CheckoutResponseDTO response = new CheckoutResponseDTO();
        response.setOrderId(order.getOrderId());
        response.setTotalAmount(order.getTotalAmount());
        response.setOrderStatus(order.getOrderStatus().toString());
        response.setItems(items);
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
    
    public List<CheckoutItemDTO> getSelectedCourses(List<Integer> courseIds) {
        return checkoutOrderRepository.findSelectedCourses(courseIds);
    }

    public Integer getUserIdByOrderId(String orderId) {
        return checkoutOrderRepository.findUserIdByOrderId(orderId)
            .orElseThrow(() -> new IllegalArgumentException("訂單不存在"));
    }
    
    public List<Integer> getCourseIdsByOrderId(String orderId) {
        return checkoutOrderRepository.findCourseIdsByOrderId(orderId);
    }
    
    public void insertPurchasedCourses(Integer userId, List<Integer> courseIds) {
        for (Integer courseId : courseIds) {
            PurchasedCourses purchasedCourse = new PurchasedCourses();
            PurchasedCoursesId id = new PurchasedCoursesId(userId.longValue(), courseId.longValue());
            purchasedCourse.setPurchasedCoursesId(id);
            purchasedCoursesRepository.save(purchasedCourse);
        }
    }
} 