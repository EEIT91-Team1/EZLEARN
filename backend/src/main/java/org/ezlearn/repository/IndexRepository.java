package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexRepository extends JpaRepository<Courses, Long> {
	
	@Query(value = "SELECT c.course_id ,c.course_name, c.course_img, c.price, ui.user_name, COUNT(p.user_id) students, AVG(p.course_rate) rate FROM courses c LEFT JOIN purchased_courses p ON c.course_id = p.course_id JOIN user_info ui ON c.teacher_id = ui.user_id GROUP BY c.course_id  ORDER BY COUNT(p.user_id) DESC LIMIT  0,12", nativeQuery = true)
	List<Object[]> searchCourse();

	@Query(value = "SELECT p.course_id ,ui.avatar,ui.user_name, p.course_review ,p.course_rate ,c.course_name ,p.updated_at FROM purchased_courses p JOIN courses c ON c.course_id = p.course_id JOIN user_info ui ON p.user_id = ui.user_id WHERE p.course_rate >0 \r\n"
			+ "ORDER BY `p`.`updated_at` DESC LIMIT 0,12", nativeQuery = true)
	List<Object[]> review();

}
