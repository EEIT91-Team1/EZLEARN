package org.ezlearn.mapper;

import org.ezlearn.model.dto.CheckoutItemDTO;
import org.ezlearn.model.entity.CheckoutOrder;
import org.ezlearn.model.entity.CheckoutOrderDetail;
import org.ezlearn.model.dto.CheckoutResponseDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CheckoutOrderMapper {
    @Select("<script>" +
            "SELECT c.course_id, c.course_name, c.course_intro, c.course_img, c.price " +
            "FROM courses c " +
            "WHERE c.course_id IN " +
            "<foreach item='id' collection='list' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    @Results({
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "courseName", column = "course_name"),
        @Result(property = "courseIntro", column = "course_intro"),
        @Result(property = "courseImg", column = "course_img"),
        @Result(property = "price", column = "price")
    })
    List<CheckoutItemDTO> getSelectedCourses(List<Integer> courseIds);

    @Insert("INSERT INTO checkout_orders (order_id, user_id, total_amount, order_status, created_at, updated_at) " +
           "VALUES (#{orderId}, #{userId}, #{totalAmount}, #{orderStatus}, #{createdAt}, #{updatedAt})")
    void insertCheckoutOrder(CheckoutOrder order);

    @Insert("INSERT INTO checkout_order_details (order_id, course_id, price, created_at) " +
           "VALUES (#{orderId}, #{courseId}, #{price}, #{createdAt})")
    void insertCheckoutOrderDetail(CheckoutOrderDetail detail);

    @Select("SELECT IFNULL(MAX(CAST(RIGHT(order_id, 4) AS UNSIGNED)), 0) + 1 " +
            "FROM checkout_orders " +
            "WHERE order_id LIKE CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), '%')")
    @Options(useCache = false)
    int getNextOrderSequence();

    @Select("SELECT COUNT(*) FROM checkout_orders WHERE order_id = #{orderId}")
    int checkOrderIdExists(@Param("orderId") String orderId);

    @Select("SELECT * FROM checkout_orders WHERE order_id = #{orderId}")
    @Results({
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "totalAmount", column = "total_amount"),
        @Result(property = "orderStatus", column = "order_status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "items", column = "order_id",
            many = @Many(select = "getOrderItems"))
    })
    CheckoutOrder getOrderDetails(@Param("orderId") String orderId);

    @Select("SELECT d.course_id, c.course_name, d.price " +
            "FROM checkout_order_details d " +
            "JOIN courses c ON d.course_id = c.course_id " +
            "WHERE d.order_id = #{orderId}")
    @Results({
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "courseName", column = "course_name"),
        @Result(property = "price", column = "price")
    })
    List<CheckoutItemDTO> getOrderItems(@Param("orderId") String orderId);

    @Update("UPDATE checkout_orders SET order_status = #{status}, " +
            "updated_at = NOW() WHERE order_id = #{orderId}")
    void updateOrderStatus(@Param("orderId") String orderId, @Param("status") String status);

    @Select("SELECT o.*, d.course_id, d.price, c.course_name, c.course_intro, c.course_img " +
            "FROM checkout_orders o " +
            "LEFT JOIN checkout_order_details d ON o.order_id = d.order_id " +
            "LEFT JOIN courses c ON d.course_id = c.course_id " +
            "WHERE o.user_id = #{userId} " +
            "ORDER BY o.created_at DESC")
    @Results({
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "totalAmount", column = "total_amount"),
        @Result(property = "orderStatus", column = "order_status"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "items", column = "order_id",
            many = @Many(select = "getOrderItems"))
    })
    List<CheckoutResponseDTO> getOrderHistory(@Param("userId") Integer userId);

    @Insert("<script>" +
            "INSERT INTO purchased_courses (course_id, user_id) VALUES " +
            "<foreach item='courseId' collection='courseIds' separator=','>" +
            "(#{courseId}, #{userId})" +
            "</foreach>" +
            "</script>")
    void insertPurchasedCourses(@Param("userId") Integer userId, 
                               @Param("courseIds") List<Integer> courseIds);

    @Select("SELECT course_id FROM checkout_order_details WHERE order_id = #{orderId}")
    List<Integer> getCourseIdsByOrderId(@Param("orderId") String orderId);

    @Select("SELECT user_id FROM checkout_orders WHERE order_id = #{orderId}")
    Integer getUserIdByOrderId(@Param("orderId") String orderId);

    @Select("SELECT COUNT(*) FROM wish_list WHERE user_id = #{userId} AND course_id = #{courseId}")
    int countCourseInWishList(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    @Delete("DELETE FROM wish_list WHERE user_id = #{userId} AND course_id = #{courseId}")
    void deleteCourseFromWishList(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
} 