package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonsRepository extends JpaRepository<Lessons, Long> {
	
	List<Lessons> findByCourses(Courses courses);
	
	Lessons findTop1ByCourses(Courses courses);
	
	List<Lessons> findByCoursesIsNull();

}
