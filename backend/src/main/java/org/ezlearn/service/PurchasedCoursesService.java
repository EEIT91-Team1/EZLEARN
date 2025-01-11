package org.ezlearn.service;

import java.math.BigDecimal;
import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.Users;
import org.ezlearn.repository.PurchasedCoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class PurchasedCoursesService {
	
	@Autowired
	private PurchasedCoursesRepository purchasedCoursesRepository;
	
	public List<PurchasedCourses> getPurchasedCoursesByUsers(Users users) {
		return purchasedCoursesRepository.findByUsers(users);
	}
	
	public List<PurchasedCourses> getPurchasedCoursesByCourses(Courses courses) {
		return purchasedCoursesRepository.findByCourses(courses);
	}
	
	public BigDecimal getAverageRateForCourse(Long courseId) {
	    return purchasedCoursesRepository.findAverageRateByCourseId(courseId);
	}
	
	public Boolean isPurchased(HttpSession session, Long courseId) {
		Users user = (Users)session.getAttribute("user");
		List<PurchasedCourses> myCourses = purchasedCoursesRepository.findByUsers(user);
		for (PurchasedCourses course : myCourses) {
			if (course.getCourses().getCourseId() == courseId) {
				return true;
			}
		}
		return false;
	}

}
