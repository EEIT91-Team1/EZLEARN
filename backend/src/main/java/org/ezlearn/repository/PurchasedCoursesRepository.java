package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.PurchasedCoursesId;
import org.ezlearn.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedCoursesRepository extends JpaRepository<PurchasedCourses, PurchasedCoursesId> {
	
	//@Query(value = "select pc.user_id, ui.user_name teachername, ui.user_intro, c.course_id, c.course_name, c.price, c.course_intro, c.course_img, c.course_type   from purchasedcourses pc join users u on u.user_id = pc.user_id join courses c on c.course_id = pc.course_id join userinfo ui on ui.user_id = c.teacher_id where u.user_id = :userId;", nativeQuery = true)
	//List<PurchasedCourses> findByUsers(@Param("userId") Long userId);
	List<PurchasedCourses> findByUsers(Users users);

}
