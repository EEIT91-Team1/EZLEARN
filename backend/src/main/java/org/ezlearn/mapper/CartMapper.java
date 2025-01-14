package org.ezlearn.mapper;

import org.ezlearn.model.dto.CartItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface CartMapper {
    
    @Select("SELECT c.user_id, c.course_id, c.created_at, " +
            "co.course_name, co.course_intro, " +
            "TO_BASE64(co.course_img) as course_img, co.price " +
            "FROM carts c " +
            "JOIN courses co ON c.course_id = co.course_id " +
            "WHERE c.user_id = #{userId}")
    @Results({
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "courseName", column = "course_name"),
        @Result(property = "courseIntro", column = "course_intro"),
        @Result(property = "courseImg", column = "course_img"),
        @Result(property = "price", column = "price"),
        @Result(property = "isSelected", column = "course_id", javaType = Boolean.class, jdbcType = JdbcType.BOOLEAN)
    })
    List<CartItemDTO> getCartItems(@Param("userId") Integer userId);
    
    @Insert("INSERT INTO carts (user_id, course_id) VALUES (#{userId}, #{courseId})")
    int addToCart(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    @Delete("DELETE FROM carts WHERE user_id = #{userId} AND course_id = #{courseId}")
    int removeFromCart(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    @Select("SELECT COUNT(*) FROM carts WHERE user_id = #{userId} AND course_id = #{courseId}")
    int checkItemExists(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    @Select("<script>" +
            "SELECT c.course_id, c.course_name, c.course_intro, c.course_img, c.price " +
            "FROM courses c " +
            "WHERE c.course_id IN " +
            "<foreach item='courseId' collection='courseIds' open='(' separator=',' close=')'>" +
            "#{courseId}" +
            "</foreach>" +
            "</script>")
    @Results({
        @Result(property = "courseId", column = "course_id"),
        @Result(property = "courseName", column = "course_name"),
        @Result(property = "courseIntro", column = "course_intro"),
        @Result(property = "courseImg", column = "course_img"),
        @Result(property = "price", column = "price")
    })
    List<CartItemDTO> getSelectedCourses(@Param("courseIds") List<Integer> courseIds);

    @Delete("<script>" +
            "DELETE FROM carts WHERE user_id = #{userId} AND course_id IN " +
            "<foreach item='courseId' collection='courseIds' open='(' separator=',' close=')'>" +
            "#{courseId}" +
            "</foreach>" +
            "</script>")
    int removeCheckedOutCourses(@Param("userId") Integer userId, @Param("courseIds") List<Integer> courseIds);
}