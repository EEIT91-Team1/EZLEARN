package org.ezlearn.controller;

import org.ezlearn.model.Courses;
import org.ezlearn.service.PostsService;
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
public class PostsController {
	
	@Autowired
	private PostsService postsService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private PurchasedCoursesService purchasedCoursesService;
	
	@Autowired
	private HttpSession httpSession;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/{courseId}/posts")
	public ResponseEntity<?> getPostsByCourses(@PathVariable Long courseId) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		if(purchasedCoursesService.isPurchased(httpSession, courseId) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		
		Courses courses = new Courses();
		courses.setCourseId(courseId);
		return ResponseEntity.ok(postsService.getPostsByCourses(courses));
	}

}
