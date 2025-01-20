package org.ezlearn.repository;

import org.ezlearn.model.Cart;
import org.ezlearn.model.CartId;
import org.ezlearn.DTO.CartItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
    List<CartItemDTO> findCartItemsByUserId(@Param("userId") Integer userId);
    
    // 檢查項目是否存在
    @Query("SELECT COUNT(c) > 0 FROM Cart c WHERE c.userId = :userId AND c.courseId = :courseId")
    boolean existsByUserIdAndCourseId(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    // 獲取選中的課程
    @Query(nativeQuery = true,
           value = "SELECT CAST(c.course_id as SIGNED) as courseId, " +
                  "c.course_name as courseName, " +
                  "CONVERT(c.course_intro USING utf8mb4) as courseIntro, " +
                  "TO_BASE64(c.course_img) as courseImg, " +
                  "c.price as price, " +
                  "0 as selected, " +
                  "0 as isPurchased " +
                  "FROM courses c " +
                  "WHERE c.course_id IN :courseIds")
    List<CartItemDTO> findSelectedCourses(@Param("courseIds") List<Integer> courseIds);
    
    // 刪除已結帳的課程
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.userId = :userId AND c.courseId IN :courseIds")
    int deleteCheckedOutCourses(@Param("userId") Integer userId, 
                               @Param("courseIds") List<Integer> courseIds);
    
    // 從購物車移除單一課程
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.userId = :userId AND c.courseId = :courseId")
    int deleteByUserIdAndCourseId(@Param("userId") Integer userId, 
                                 @Param("courseId") Integer courseId);
} 