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
@RequestMapping("/teachers")
public class CoursesController {
	
	@Autowired
	private CoursesService coursesService;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/{teacher_id}/courses")
	public List<Courses> getCoursesByTeacherId(@PathVariable Long teacher_id) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(teacher_id);
		return coursesService.getCoursesByUsers(userInfo);
	}

}
