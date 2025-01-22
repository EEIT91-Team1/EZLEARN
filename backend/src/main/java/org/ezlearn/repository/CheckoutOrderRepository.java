package org.ezlearn.repository;

import org.ezlearn.DTO.CheckoutItemDTO;
import org.ezlearn.model.CheckoutOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckoutOrderRepository extends JpaRepository<CheckoutOrder, String> {
    
    // 獲取選中的課程
	public final static String SQL1 = "SELECT CAST(c.course_id AS SIGNED) as courseId, " +
            "c.course_name as courseName, " +
            "c.course_intro as courseIntro, " +
            "TO_BASE64(c.course_img) as courseImg, " +
            "c.price as price " +
            "FROM courses c " +
            "WHERE c.course_id IN :courseIds";
    @Query(nativeQuery = true, value = SQL1)
    List<CheckoutItemDTO> findSelectedCourses(@Param("courseIds") List<Long> courseIds);
    
    // 獲取下一個訂單序號
    @Query(value = "SELECT IFNULL(MAX(CAST(RIGHT(order_id, 4) AS UNSIGNED)), 0) + 1 " +
           "FROM checkout_orders " +
           "WHERE order_id LIKE CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '%')", 
           nativeQuery = true)
    int getNextOrderSequence();
    
    // 檢查訂單ID是否存在
    boolean existsByOrderId(String orderId);
    
    // 獲取訂單詳情
    @Query("SELECT o FROM CheckoutOrder o LEFT JOIN FETCH o.items i WHERE o.orderId = :orderId")
    Optional<CheckoutOrder> findOrderWithItems(@Param("orderId") String orderId);
    
    // 更新訂單狀態
    @Modifying
    @Query("UPDATE CheckoutOrder o SET o.orderStatus = :status, o.updatedAt = CURRENT_TIMESTAMP " +
           "WHERE o.orderId = :orderId")
    void updateOrderStatus(@Param("orderId") String orderId, 
                         @Param("status") CheckoutOrder.OrderStatus status);
    
    // 獲取訂單歷史
    @Query("SELECT o FROM CheckoutOrder o " +
           "LEFT JOIN FETCH o.items " +
           "WHERE o.userId = :userId " +
           "ORDER BY o.createdAt DESC")
    List<CheckoutOrder> findOrderHistoryByUserId(@Param("userId") Integer userId);
    
    // 根據訂單ID獲取用戶ID
    @Query("SELECT o.userId FROM CheckoutOrder o WHERE o.orderId = :orderId")
    Optional<Integer> findUserIdByOrderId(@Param("orderId") String orderId);
    
    // 根據訂單ID獲取課程ID列表
    @Query("SELECT d.courseId FROM CheckoutOrderDetail d WHERE d.orderId = :orderId")
    List<Integer> findCourseIdsByOrderId(@Param("orderId") String orderId);
    
    @Query(value = "INSERT INTO purchased_courses (course_id, user_id) " +
           "SELECT :courseId, :userId " +
           "FROM dual " +
           "WHERE NOT EXISTS (SELECT 1 FROM purchased_courses WHERE course_id = :courseId AND user_id = :userId)", 
           nativeQuery = true)
    @Modifying
    void insertPurchasedCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(w) FROM WishList w WHERE w.userId = :userId AND w.courseId = :courseId")
    int countCourseInWishList(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    @Modifying
    @Query("DELETE FROM WishList w WHERE w.userId = :userId AND w.courseId = :courseId")
    void deleteCourseFromWishList(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    @Query(value = "SELECT COUNT(1) FROM checkout_orders o " +
           "JOIN checkout_order_details d ON o.order_id = d.order_id " +
           "WHERE o.user_id = :userId " +
           "AND o.order_status = 'COMPLETE' " +
           "AND d.course_id IN " +
           "(SELECT d2.course_id FROM checkout_order_details d2 WHERE d2.order_id = :orderId)",
           nativeQuery = true)
    Long countPaidOrderWithSameCourses(@Param("userId") Long userId, 
                                     @Param("orderId") String orderId);
    
    // 根據訂單狀態查詢訂單
    List<CheckoutOrder> findByOrderStatus(CheckoutOrder.OrderStatus orderStatus);
} 