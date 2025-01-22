package org.ezlearn.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Progress;
import org.ezlearn.model.ProgressId;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.UserInfo;
import org.ezlearn.model.Users;
import org.ezlearn.repository.LessonsRepository;
import org.ezlearn.repository.ProgressRepository;
import org.ezlearn.repository.PurchasedCoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgressService {
	
	@Autowired
	private ProgressRepository progressRepository;
	
	@Autowired
	private LessonsRepository lessonsRepository;
	
	@Autowired
	private PurchasedCoursesRepository purchasedCoursesRepository;
	
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

	public List<Map<String,String>> getUserProgress(Users user){
		List<PurchasedCourses> purchasedCourses = purchasedCoursesRepository.findByUsers(user);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(PurchasedCourses course : purchasedCourses ) {
		Map<String, String> map=new HashMap<String, String>();
		Double completedPercentage= progressRepository.calculateCompletedPercentageByUserIdAndLessonsCourseId(user.getUserId(), course.getCourses().getCourseId());
		if (completedPercentage == null) {
			completedPercentage = 0.0;
		}
		map.put("courseId",course.getCourses().getCourseId()+"");
		map.put("courseName",course.getCourses().getCourseName());
		map.put("teacher",course.getCourses().getUserInfo().getUserName());
		map.put("completedPercentage",Math.round(completedPercentage)+"");
		String imgBase64 = "data:image/png;base64,"
				+ Base64.getEncoder().encodeToString(course.getCourses().getCourseImg());
		map.put("courseImg",imgBase64);
		list.add(map);
		}
		return list;
	}
}
