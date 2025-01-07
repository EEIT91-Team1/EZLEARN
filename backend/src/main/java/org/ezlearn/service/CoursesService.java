package org.ezlearn.service;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.UserInfo;
import org.ezlearn.repository.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoursesService {
	
	@Autowired
	private CoursesRepository coursesRepository;
	
	public List<Courses> getCoursesByUsers(UserInfo userInfo) {
		return coursesRepository.findByUserInfo(userInfo);
	}
	
	public List<Courses> getCoursesByCourseId(Long courseId) {
		return coursesRepository.findByCourseId(courseId);
	}

}
