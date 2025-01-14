package org.ezlearn.controller;

import org.ezlearn.service.CheckoutOrderService;
import org.ezlearn.service.CartService;
import org.ezlearn.model.dto.CheckoutResponseDTO;
import org.ezlearn.model.dto.CheckoutRequest;
import org.ezlearn.model.dto.ApiResponse;
import org.ezlearn.service.UsersService;
import org.ezlearn.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@Slf4j
public class CheckoutOrderController {
    
    @Autowired
    private CheckoutOrderService checkoutOrderService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UsersService usersService;
    
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<ApiResponse<CheckoutResponseDTO>> createCheckoutOrder(
            @RequestBody CheckoutRequest request,
            HttpSession session) {
        try {
            log.info("開始創建訂單 - Session ID: {}", session.getId());
            
            // 1. 檢查登入狀態
            if (!usersService.islogin(session)) {
                log.warn("用戶未登入 - Session ID: {}", session.getId());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
            }

            // 2. 基本驗證
            if (request.getCourseIds() == null || request.getCourseIds().isEmpty()) {
                log.warn("請求的課程ID列表為空");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "請選擇要購買的課程"));
            }

            // 3. 從 session 獲取用戶信息
            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                log.warn("Session中無用戶信息 - Session ID: {}", session.getId());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請重新登入"));
            }
            
            Integer userId = user.getUserId().intValue();
            log.info("用戶信息 - userId: {}, email: {}", userId, user.getEmail());
            
            // 4. 添加詳細的請求日誌
            log.info("收到創建訂單請求 - userId: {}, courseIds: {}, request: {}", 
                    userId, request.getCourseIds(), request);
            
            // 5. 創建訂單
            CheckoutResponseDTO response = checkoutOrderService.createCheckoutOrder(userId, request.getCourseIds());
            
            // 6. 只有當訂單成功創建後，才從購物車中移除已購買的課程
            if (response != null && response.getOrderId() != null) {
                cartService.removeCheckedOutCourses(userId, request.getCourseIds());
                log.info("成功從購物車移除已購買課程 - userId: {}, courseIds: {}", 
                        userId, request.getCourseIds());
                
                // 7. 記錄成功日誌
                log.info("訂單創建成功 - orderId: {}, totalAmount: {}, items: {}", 
                        response.getOrderId(), 
                        response.getTotalAmount(), 
                        response.getItems());
                
                return ResponseEntity.ok(ApiResponse.success(response));
            } else {
                throw new IllegalStateException("訂單創建失敗：未返回有效的訂單信息");
            }
     
        } catch (IllegalArgumentException e) {
            // 8. 記錄業務邏輯錯誤
            log.warn("創建訂單業務邏輯錯誤 - error: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            // 9. 記錄詳細錯誤信息
            log.error("創建結帳訂單失敗 - error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "創建結帳訂單失敗，請稍後再試"));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<CheckoutResponseDTO>> getOrderDetails(
            @PathVariable String orderId) {
        try {
            CheckoutResponseDTO response = checkoutOrderService.getOrderDetails(orderId);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (IllegalArgumentException e) {
            log.warn("獲取訂單詳情失敗: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            log.error("獲取訂單詳情失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "獲取訂單詳情失敗"));
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam String status) {
        try {
            checkoutOrderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            log.error("更新訂單狀態失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "更新訂單狀態失敗"));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<CheckoutResponseDTO>>> getOrderHistory() {
        try {
            Integer userId = 1; // TODO: 從認證中獲取
            List<CheckoutResponseDTO> history = checkoutOrderService.getOrderHistory(userId);
            return ResponseEntity.ok(ApiResponse.success(history));
        } catch (Exception e) {
            log.error("獲取訂單歷史失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "獲取訂單歷史失敗"));
        }
    }

    @GetMapping("/{orderId}/verify")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> verifyOrderAmount(
            @PathVariable String orderId) {
        try {
            CheckoutResponseDTO order = checkoutOrderService.getOrderDetails(orderId);
            
            // 檢查訂單狀態
            if (!"PENDING".equals(order.getOrderStatus())) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "訂單狀態已改變，請重新確認"));
            }
            
            Map<String, Integer> response = new HashMap<>();
            response.put("totalAmount", order.getTotalAmount());
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            log.error("訂單金額驗證失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "訂單金額驗證失敗"));
        }
    }
} 