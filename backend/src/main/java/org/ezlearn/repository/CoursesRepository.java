package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursesRepository extends JpaRepository<Courses, Long> {
	
	List<Courses> findByUserInfo(UserInfo userInfo);
	
	Courses findByCourseId(Long courseId);

}
