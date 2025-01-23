package org.ezlearn.service;

import org.ezlearn.DTO.CheckoutItemDTO;
import org.ezlearn.DTO.CheckoutResponseDTO;
import org.ezlearn.model.CheckoutOrder;
import org.ezlearn.model.CheckoutOrderDetail;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.repository.CheckoutOrderRepository;
import org.ezlearn.repository.CheckoutOrderDetailRepository;
import org.ezlearn.repository.PurchasedCoursesRepository;
import org.ezlearn.repository.WishListRepository;
import org.ezlearn.model.CheckoutOrder.OrderStatus;
import org.ezlearn.model.PurchasedCoursesId;
import org.ezlearn.repository.CartRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Service
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
        try {
            // 獲取所有待處理的訂單
            List<CheckoutOrder> pendingOrders = checkoutOrderRepository.findByOrderStatus(OrderStatus.PENDING);
            
            for (CheckoutOrder order : pendingOrders) {
                // 檢查是否有相同商品的已付款訂單
                Long count = checkoutOrderRepository.countPaidOrderWithSameCourses(order.getUserId(), order.getOrderId());
                if (count != null && count > 0) {
                    // 如果存在相同商品的已付款訂單，將當前訂單標記為取消
                    order.setOrderStatus(OrderStatus.CANCELLED);
                    order.setUpdatedAt(LocalDateTime.now());
                    checkoutOrderRepository.save(order);
                }
            }
        } catch (Exception e) {
        }
    }
    
    @Transactional
    public CheckoutResponseDTO createCheckoutOrder(Long userId, List<Long> courseIds) {
        try {
            // 1. 從資料庫獲取最新價格
            List<CheckoutItemDTO> items = checkoutOrderRepository.findSelectedCourses(courseIds);
            
            // 2. 驗證所有課程是否存在
            if (items.size() != courseIds.size()) {
                throw new IllegalArgumentException("部分課程不存在或已下架");
            }
            
            // 3. 計算總金額
            int totalAmount = items.stream()
                    .mapToInt(CheckoutItemDTO::getPrice)
                    .sum();
            
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
            
            checkoutOrderRepository.save(order);
            
            return response;
            
        } catch (Exception e) {
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
                return orderId;
            }
        }
        
        throw new RuntimeException("無法生成唯一的訂單編號");
    }
    
    public CheckoutResponseDTO getOrderDetails(String orderId) {
        try {
            CheckoutOrder order = checkoutOrderRepository.findOrderWithItems(orderId)
                .orElseThrow(() -> new IllegalArgumentException("訂單不存在"));
            
            // 獲取所有課程ID
            List<Long> courseIds = order.getItems().stream()
                .map(CheckoutOrderDetail::getCourseId)
                .collect(Collectors.toList());
            
            // 獲取課程詳細信息
            List<CheckoutItemDTO> items = checkoutOrderRepository.findSelectedCourses(courseIds);
            
            // 將價格信息從訂單明細中補充到課程信息中
            Map<Long, Integer> coursePriceMap = order.getItems().stream()
                .collect(Collectors.toMap(
                    CheckoutOrderDetail::getCourseId,
                    CheckoutOrderDetail::getPrice
                ));
            
            items.forEach(item -> item.setPrice(coursePriceMap.get(item.getCourseId())));
            
            CheckoutResponseDTO response = buildCheckoutResponse(order, items);
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("獲取訂單詳情失敗: " + e.getMessage());
        }
    }
    
    @Transactional
    public void updateOrderStatus(String orderId, OrderStatus status) {
        CheckoutOrder order = checkoutOrderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("訂單不存在"));
        
        // 檢查是否有相同商品的已付款訂單
        if (status == OrderStatus.COMPLETE) {
            Long count = checkoutOrderRepository.countPaidOrderWithSameCourses(order.getUserId(), orderId);
            if (count != null && count > 0) {
                // 如果存在相同商品的已付款訂單，將當前訂單標記為付款失敗
                order.setOrderStatus(OrderStatus.CANCELLED);
                order.setUpdatedAt(LocalDateTime.now());
                checkoutOrderRepository.save(order);
                return;
            }
        }
        
        order.setOrderStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        
        if (status == OrderStatus.COMPLETE) {
            // 處理已付款訂單
            List<Long> courseIds = order.getItems().stream()
                .map(CheckoutOrderDetail::getCourseId)
                .collect(Collectors.toList());
                
            // 添加到已購買課程
            for (Long courseId : courseIds) {
                try {
                    checkoutOrderRepository.insertPurchasedCourse(order.getUserId(), courseId);
                    
                    // 從願望清單中移除
                    try {
                        wishListRepository.deleteWish(order.getUserId().toString(), courseId.toString());
                    } catch (Exception e) {
                    }
                } catch (Exception e) {
                }
            }
            
            // 從購物車中移除
            cartRepository.deleteCheckedOutCourses(order.getUserId(), courseIds);
        }
        
        checkoutOrderRepository.save(order);
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
                }
            }
        }
        
        // 重新獲取更新後的訂單列表
        orders = checkoutOrderRepository.findOrderHistoryByUserId(userId);
        
        return orders.stream()
            .map(order -> {
                // 獲取所有課程ID
                List<Long> courseIds = order.getItems().stream()
                    .map(CheckoutOrderDetail::getCourseId)
                    .collect(Collectors.toList());
                
                // 獲取課程詳細信息
                List<CheckoutItemDTO> items = checkoutOrderRepository.findSelectedCourses(courseIds);
                
                // 將價格信息從訂單明細中補充到課程信息中
                Map<Long, Integer> coursePriceMap = order.getItems().stream()
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
    
    public List<CheckoutItemDTO> getSelectedCourses(List<Long> courseIds) {
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
    
    public List<Map<String, String>> getOrderHistoryByCourseId(Long courseId){
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		List<CheckoutOrderDetail> list =  checkoutOrderDetailRepository.findByCourseId(courseId);
		for(CheckoutOrderDetail order : list) {
		Map<String,String> map =new HashMap<String, String>();
		map.put("orderId",order.getOrderId());
 		map.put("price",order.getPrice()+"");
 		map.put("courseId",order.getCourseId()+"");
 		map.put("createdAt",order.getCreatedAt()+"");
 		dataList.add(map);
	}
	return dataList;
}

} 