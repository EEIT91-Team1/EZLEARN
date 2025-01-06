package org.ezlearn.service;

import java.util.List;

import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.Users;
import org.ezlearn.repository.PurchasedCoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchasedCoursesService {
	
	@Autowired
	private PurchasedCoursesRepository purchasedCoursesRepository;
	
	public List<PurchasedCourses> getPurchasedCoursesByUsers(Users users) {
		return purchasedCoursesRepository.findByUsers(users);
	}

}
