package org.ezlearn.controller;

import org.ezlearn.service.CartService;
import org.ezlearn.DTO.ApiResponse;
import org.ezlearn.DTO.CartResponseDTO;
import org.ezlearn.DTO.CheckoutRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ezlearn.service.UsersService;
import org.ezlearn.model.Users;

import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UsersService usersService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCartItems(HttpSession session) {
        
        // 檢查登入狀態
        if (!usersService.islogin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "請先登入"));
        }
        
        try {
            Users user = (Users)session.getAttribute("user");
            CartResponseDTO response = cartService.getCartItems(user.getUserId());
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
        	System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "獲取購物車失敗"));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<CartResponseDTO>> addToCart(
            @RequestParam Long courseId,
            HttpSession session) {
        // 檢查登入狀態
        if (!usersService.islogin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "請先登入"));
        }
        
        try {
            Users user = (Users)session.getAttribute("user");
            boolean success = cartService.addToCart(user.getUserId(), courseId);
            if (success) {
                CartResponseDTO updatedCart = cartService.getCartItems(user.getUserId());
                return ResponseEntity.ok(ApiResponse.success(updatedCart));
            } else {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "課程已在購物車中"));
            }
        } catch (Exception e) {
        	System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "加入購物車失敗"));
        }
    }
    
    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> removeFromCart(
            @PathVariable Long courseId,
            HttpSession session) {
        // 檢查登入狀態
        if (!usersService.islogin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "請先登入"));
        }
        
        try {
            Users user = (Users)session.getAttribute("user");
            CartResponseDTO updatedCart = cartService.removeFromCart(user.getUserId(), courseId);
            return ResponseEntity.ok(ApiResponse.success(updatedCart));
        } catch (Exception e) {
        	System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "從購物車移除失敗"));
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> verifyPrice(@RequestBody CheckoutRequest request) {
        try {
            if (request.getCourseIds() == null || request.getCourseIds().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "請選擇要購買的課程"));
            }

            // 獲取最新的課程價格總和
            int totalAmount = cartService.calculateTotal(request.getCourseIds());
            
            Map<String, Integer> response = new HashMap<>();
            response.put("totalAmount", totalAmount);
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "價格驗證失敗"));
        }
    }
} 