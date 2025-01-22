package org.ezlearn.repository;

import org.ezlearn.model.Cart;
import org.ezlearn.model.CartId;
import org.ezlearn.DTO.CartItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, CartId> {
    
    // 獲取購物車項目
    @Query(nativeQuery = true,
           value = "SELECT CAST(c.course_id as SIGNED) as courseId, " +
                  "co.course_name as courseName, " +
                  "CONVERT(co.course_intro USING utf8mb4) as courseIntro, " +
                  "TO_BASE64(co.course_img) as courseImg, " +
                  "co.price as price, " +
                  "0 as selected, " +
                  "CASE WHEN pc.course_id IS NOT NULL THEN 1 ELSE 0 END as isPurchased " +
                  "FROM carts c " +
                  "JOIN courses co ON c.course_id = co.course_id " +
                  "LEFT JOIN purchased_courses pc ON c.course_id = pc.course_id AND pc.user_id = c.user_id " +
                  "WHERE c.user_id = :userId")
    List<CartItemDTO> findCartItemsByUserId(@Param("userId") Long userId);
    
    // 檢查項目是否存在
    @Query("SELECT COUNT(c) > 0 FROM Cart c WHERE c.cartId.userId = :userId AND c.cartId.courseId = :courseId")
    boolean existsByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    // 獲取選中的課程
    public final static String SQL1 = "SELECT CAST(c.course_id as SIGNED) as courseId, " +
            "c.course_name as courseName, " +
            "CONVERT(c.course_intro USING utf8mb4) as courseIntro, " +
            "TO_BASE64(c.course_img) as courseImg, " +
            "c.price as price, " +
            "0 as selected, " +
            "0 as isPurchased " +
            "FROM courses c " +
            "WHERE c.course_id IN :courseIds";
    @Query(nativeQuery = true,
           value = SQL1)
    List<CartItemDTO> findSelectedCourses(@Param("courseIds") List<Long> courseIds);
    
    // 刪除已結帳的課程
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.cartId.userId = :userId AND c.cartId.courseId IN :courseIds")
    int deleteCheckedOutCourses(@Param("userId") Long userId, 
                               @Param("courseIds") List<Long> courseIds);
    
    // 從購物車移除單一課程
    @Transactional
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.cartId.userId = :userId AND c.cartId.courseId = :courseId")
    int deleteByUserIdAndCourseId(@Param("userId") Long userId, 
                                 @Param("courseId") Long courseId);
} 