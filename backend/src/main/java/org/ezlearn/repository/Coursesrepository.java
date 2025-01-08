package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Coursesrepository extends JpaRepository<Courses, Long>{
	List<Courses> findByTeacherId(Long teacherid);
	Courses findByCourseId(Long courseId);
}
