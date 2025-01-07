package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Questions;
import org.ezlearn.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Questionrepository extends JpaRepository<Questions, Long>{
//	@Query(value = "select * from questions where lesson_id = ?1",nativeQuery = true)
	List<Questions> findByLesson(Lessons lesson);
}
