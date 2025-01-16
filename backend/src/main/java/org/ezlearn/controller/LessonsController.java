package org.ezlearn.controller;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.service.LessonsService;
import org.ezlearn.service.PurchasedCoursesService;
import org.ezlearn.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/courses")
public class LessonsController {
	
	@Autowired
	private LessonsService lessonsService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private PurchasedCoursesService purchasedCoursesService;
	
	@Autowired
	private HttpSession httpSession;
	
	@GetMapping("/{courseId}/lessons")
	public ResponseEntity<?> getLessonsByCourses(@PathVariable Long courseId) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		if(purchasedCoursesService.isPurchased(httpSession, courseId) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		
		Courses courses = new Courses();
		courses.setCourseId(courseId);
		return ResponseEntity.ok(lessonsService.getLessonsByCourses(courses));
	}
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/{courseId}/preview")
	public Lessons getTop1LessonsByCourses(@PathVariable Long courseId) {
		Courses course = new Courses();
		course.setCourseId(courseId);
		return lessonsService.getTop1LessonsByCourses(course);
	}

}
