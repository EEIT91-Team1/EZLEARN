package org.ezlearn.controller;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.service.LessonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
public class LessonsController {
	
	@Autowired
	private LessonsService lessonsService;
	
	@GetMapping("/{courseId}/lessons")
	public List<Lessons> getLessonsByCourses(@PathVariable Long courseId) {
		Courses courses = new Courses();
		courses.setCourseId(courseId);
		return lessonsService.getLessonsByCourses(courses);
	}

}
