package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Progress;
import org.ezlearn.model.ProgressId;
import org.ezlearn.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, ProgressId> {
	
	Progress findByUsersAndLessons(Users users, Lessons lessons);
	
	List<Progress> findByUsersAndLessonsCourses(Users users, Courses courses);
	
    Progress findTopByUsersAndLessonsInOrderByUpdatedAtDesc(Users users, List<Lessons> lessons);

}
