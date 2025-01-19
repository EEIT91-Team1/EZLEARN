package org.ezlearn.service;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Progress;
import org.ezlearn.model.ProgressId;
import org.ezlearn.model.Users;
import org.ezlearn.repository.LessonsRepository;
import org.ezlearn.repository.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgressService {
	
	@Autowired
	private ProgressRepository progressRepository;
	
	@Autowired
	private LessonsRepository lessonsRepository;
	
	public Progress getProgressByUsersAndLessons(Users users, Lessons lessons) {
		return progressRepository.findByUsersAndLessons(users, lessons);
	}
	
	public List<Progress> getProgressByUsersAndCourses(Users users, Courses courses) {
		return progressRepository.findByUsersAndLessonsCourses(users, courses);
	}
	
	@Transactional
	public Progress saveProgress(Progress progress) {
		return progressRepository.save(progress);
	}
	
	public Progress getLastView(Users users, Courses courses) {
		List<Lessons> lessons = lessonsRepository.findByCourses(courses);
		return progressRepository.findTopByUsersAndLessonsInOrderByUpdatedAtDesc(users, lessons);
	}
	
	@Transactional
	public int updateProgress(ProgressId progressId, Integer progressTime, Integer totalDuration) {
        return progressRepository.updateByUsersAndLessons(progressId, progressTime, totalDuration);
    }
	 
	@Transactional
    public void markProgressAsCompleted(Progress progress) {
        progress.setIsCompleted(true);
        progressRepository.save(progress);
    }
	
	public Double getCompletedPercentageByUserAndCourse(Long userId, Long courseId) {
        return progressRepository.calculateCompletedPercentageByUserIdAndLessonsCourseId(userId, courseId);
    }

}
