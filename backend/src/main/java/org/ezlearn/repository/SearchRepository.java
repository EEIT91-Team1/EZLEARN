package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Courses, Long> {
	
	List<Courses> findBycourseNameContaining(String key);
	
	public final static String SQL1 = "SELECT COUNT(course_id), " + "COUNT(CASE WHEN course_type = '語言學習' THEN 1 END) AS language_count, "
			+ "COUNT(CASE WHEN course_type = '程式設計' THEN 1 END), "
			+ "COUNT(CASE WHEN course_type = '美食料理' THEN 1 END), "
			+ "COUNT(CASE WHEN course_type = '藝術創作' THEN 1 END), "
			+ "COUNT(CASE WHEN course_type = '運動健身' THEN 1 END), " + "COUNT(CASE WHEN course_type = '理財投資' THEN 1 END) "
			+ "FROM courses JOIN user_info on courses.teacher_id=user_info.user_id " + "WHERE course_name LIKE ?1 "
			+ "OR course_intro LIKE ?1 " + "OR course_type LIKE ?1 "
			+ "OR user_info.user_name LIKE ?1 ";
	@Query(value = SQL1, nativeQuery = true)
	List<Object[]> searchCount(String courseName); 	// 查詢總筆數
	
	
	public final static String SQL2 = "SELECT c.course_name, c.course_intro, c.course_img, c.price, ui.user_name, COUNT(p.user_id) students, AVG(p.course_rate) rate,c.course_id "
			+ "FROM courses c " + "LEFT JOIN purchased_courses p ON c.course_id = p.course_id "
			+ "JOIN user_info ui ON c.teacher_id = ui.user_id " + "WHERE (course_name LIKE ?1 "
			+ "OR c.course_intro LIKE ?1 " + "OR c.course_type LIKE ?1 " + "OR ui.user_name LIKE ?1) "
			+ "AND (c.course_type = ?3 OR ?3 IS NULL) " + "AND (c.price <= ?4 OR ?4 IS NULL) " + "GROUP BY c.course_id "
			+ "HAVING ( ?5 IS NULL OR AVG(p.course_rate) >= ?5) " + "ORDER BY COUNT(p.user_id) DESC "
			+ "LIMIT ?2, 11";
	@Query(value = SQL2, nativeQuery = true)
	List<Object[]> searchOrderByStudents(String courseName, Integer page, String corseType, String price, String rate);
	
	
	public final static String SQL3 = "SELECT c.course_name, c.course_intro, c.course_img, c.price, ui.user_name, COUNT(p.user_id) students, AVG(p.course_rate) rate,c.course_id "
			+ "FROM courses c " + "LEFT JOIN purchased_courses p ON c.course_id = p.course_id "
			+ "JOIN user_info ui ON c.teacher_id = ui.user_id " + "WHERE (course_name LIKE ?1 "
			+ "OR c.course_intro LIKE ?1 " + "OR c.course_type LIKE ?1 " + "OR ui.user_name LIKE ?1) "
			+ "AND (c.course_type = ?3 OR ?3 IS NULL) " + "AND (c.price <= ?4 OR ?4 IS NULL) " + "GROUP BY c.course_id "
			+ "HAVING ( ?5 IS NULL OR AVG(p.course_rate) >= ?5) " + "ORDER BY c.course_id DESC "
			+ "LIMIT ?2, 11";
	@Query(value = SQL3, nativeQuery = true)
	List<Object[]> searchOrderByCourseId(String courseName, Integer page, String corseType, String price, String rate);

	
	public final static String SQL4 = "SELECT c.course_name, c.course_intro, c.course_img, c.price, ui.user_name, COUNT(p.user_id) students, AVG(p.course_rate) rate,c.course_id "
			+ "FROM courses c " + "LEFT JOIN purchased_courses p ON c.course_id = p.course_id "
			+ "JOIN user_info ui ON c.teacher_id = ui.user_id " + "WHERE (course_name LIKE ?1 "
			+ "OR c.course_intro LIKE ?1 " + "OR c.course_type LIKE ?1 " + "OR ui.user_name LIKE ?1) "
			+ "AND (c.course_type = ?3 OR ?3 IS NULL) " + "AND (c.price <= ?4 OR ?4 IS NULL) " + "GROUP BY c.course_id "
			+ "HAVING ( ?5 IS NULL OR AVG(p.course_rate) >= ?5) " + "ORDER BY AVG(p.course_rate) DESC "
			+ "LIMIT ?2, 11";
	@Query(value = SQL4, nativeQuery = true)
	List<Object[]> searchOrderByRate(String courseName, Integer page, String corseType, String price, String rate);

	
	public final static String SQL5 = "SELECT c.course_name, c.course_intro, c.course_img, c.price, ui.user_name, COUNT(p.user_id) students, AVG(p.course_rate) rate,c.course_id "
			+ "FROM courses c " + "LEFT JOIN purchased_courses p ON c.course_id = p.course_id "
			+ "JOIN user_info ui ON c.teacher_id = ui.user_id " + "WHERE (course_name LIKE ?1 "
			+ "OR c.course_intro LIKE ?1 " + "OR c.course_type LIKE ?1 " + "OR ui.user_name LIKE ?1) "
			+ "AND (c.course_type = ?3 OR ?3 IS NULL) " + "AND (c.price <= ?4 OR ?4 IS NULL) " + "GROUP BY c.course_id "
			+ "HAVING ( ?5 IS NULL OR AVG(p.course_rate) >= ?5) " + "ORDER BY c.price DESC "
			+ "LIMIT ?2, 11";
	@Query(value = SQL5, nativeQuery = true)
	List<Object[]> searchOrderByPriceT(String courseName, Integer page, String corseType, String price, String rate);

	
	public final static String SQL6 = "SELECT c.course_name, c.course_intro, c.course_img, c.price, ui.user_name, COUNT(p.user_id) students, AVG(p.course_rate) rate,c.course_id "
			+ "FROM courses c " + "LEFT JOIN purchased_courses p ON c.course_id = p.course_id "
			+ "JOIN user_info ui ON c.teacher_id = ui.user_id " + "WHERE (course_name LIKE ?1 "
			+ "OR c.course_intro LIKE ?1 " + "OR c.course_type LIKE ?1 " + "OR ui.user_name LIKE ?1) "
			+ "AND (c.course_type = ?3 OR ?3 IS NULL) " + "AND (c.price <= ?4 OR ?4 IS NULL) " + "GROUP BY c.course_id "
			+ "HAVING ( ?5 IS NULL OR AVG(p.course_rate) >= ?5) " + "ORDER BY c.price ASC "
			+ "LIMIT ?2, 11";
	@Query(value = SQL6, nativeQuery = true)
	List<Object[]> searchOrderByPriceB(String courseName, Integer page, String corseType, String price, String rate);

}
