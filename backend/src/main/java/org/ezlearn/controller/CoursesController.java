package org.ezlearn.controller;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.UserInfo;
import org.ezlearn.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
public class CoursesController {
	
	@Autowired
	private CoursesService coursesService;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/teacher/{teacherId}")
	public List<Courses> getCoursesByTeacherId(@PathVariable Long teacherId) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(teacherId);
		return coursesService.getCoursesByUsers(userInfo);
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/{courseId}")
	public Courses getCoursesByCourseId(@PathVariable Long courseId) {
		Courses course = new Courses();
		course.setCourseId(courseId);
		return coursesService.getCoursesByCourseId(courseId);
	}

}
