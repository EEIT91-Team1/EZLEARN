package org.ezlearn.repository;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Progress;
import org.ezlearn.model.ProgressId;
import org.ezlearn.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, ProgressId> {
	
	Progress findByUsersAndLessons(Users users, Lessons lessons);
	
	List<Progress> findByUsersAndLessonsCourses(Users users, Courses courses);
	
    Progress findTopByUsersAndLessonsInOrderByUpdatedAtDesc(Users users, List<Lessons> lessons);
    
    @Modifying
    @Transactional
    @Query("UPDATE Progress p SET p.progressTime = :progressTime, " +
    	       "p.totalDuration = :totalDuration, " +
    	       "p.progressPercent = (:progressTime * 100) / :totalDuration " +
    	       "WHERE p.progressId = :progressId")
    int updateByUsersAndLessons(@Param("progressId") ProgressId progressId, 
    							@Param("progressTime") Integer progressTime,
            					@Param("totalDuration") Integer totalDuration);
    
    @Query("SELECT ROUND((COUNT(p) * 100.0) / (SELECT COUNT(l) FROM Lessons l WHERE l.courses.courseId = :courseId)) " +
            "FROM Progress p WHERE p.isCompleted = true AND p.users.userId = :userId AND p.lessons.courses.courseId = :courseId")
     Double calculateCompletedPercentageByUserIdAndLessonsCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);

}
