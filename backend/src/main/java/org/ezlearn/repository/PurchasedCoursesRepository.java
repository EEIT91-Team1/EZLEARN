package org.ezlearn.repository;

import java.math.BigDecimal;
import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.PurchasedCoursesId;
import org.ezlearn.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedCoursesRepository extends JpaRepository<PurchasedCourses, PurchasedCoursesId> {

	List<PurchasedCourses> findByUsers(Users users);
	
	List<PurchasedCourses> findByCourses(Courses courses);
	
	PurchasedCourses findByCoursesAndUsers (Courses courses, Users users);
	
	@Query("SELECT ROUND(COALESCE(AVG(p.courseRate), 0), 1) FROM PurchasedCourses p WHERE p.courses.courseId = :courseId")
	BigDecimal findAverageRateByCourseId(@Param("courseId") Long courseId);

}
