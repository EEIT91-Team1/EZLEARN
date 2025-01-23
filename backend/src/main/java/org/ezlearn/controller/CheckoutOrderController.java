package org.ezlearn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezlearn.DTO.ApiResponse;
import org.ezlearn.DTO.CheckoutRequest;
import org.ezlearn.DTO.CheckoutResponseDTO;
import org.ezlearn.model.CheckoutOrder;
import org.ezlearn.model.CheckoutOrderDetail;
import org.ezlearn.model.Users;
import org.ezlearn.repository.CheckoutOrderDetailRepository;
import org.ezlearn.service.CartService;
import org.ezlearn.service.CheckoutOrderService;
import org.ezlearn.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
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

            if (!usersService.islogin(session)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
            }

            if (request.getCourseIds() == null || request.getCourseIds().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "請選擇要購買的課程"));
            }

            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請重新登入"));
            }
            
            Long userId = user.getUserId();
            
            CheckoutResponseDTO response = checkoutOrderService.createCheckoutOrder(userId, request.getCourseIds());
            
            if (response != null && response.getOrderId() != null) {
                cartService.removeCheckedOutCourses(userId, request.getCourseIds());
                return ResponseEntity.ok(ApiResponse.success(response));
            } else {
                throw new IllegalStateException("訂單創建失敗：未返回有效的訂單信息");
            }
     
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
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
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "獲取訂單詳情失敗"));
        }
    }

    @PutMapping("/{orderId}/status")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam String status) {
        try {
            CheckoutOrder.OrderStatus orderStatus = CheckoutOrder.OrderStatus.valueOf(status);
            checkoutOrderService.updateOrderStatus(orderId, orderStatus);
            
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "無效的訂單狀態"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "更新訂單狀態失敗"));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<CheckoutResponseDTO>>> getOrderHistory(HttpSession session) {
        try {
            if (!usersService.islogin(session)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請先登入"));
            }

            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "請重新登入"));
            }

            Integer userId = user.getUserId().intValue();
            List<CheckoutResponseDTO> history = checkoutOrderService.getOrderHistory(userId);
            return ResponseEntity.ok(ApiResponse.success(history));
        } catch (Exception e) {
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "訂單金額驗證失敗"));
        }
    }

    @GetMapping("/course/{courseId}")
    public  List<Map<String,String>> getByCourseId(@PathVariable String courseId){
    	return checkoutOrderService.getOrderHistoryByCourseId(Long.parseLong(courseId));
    }
} 
