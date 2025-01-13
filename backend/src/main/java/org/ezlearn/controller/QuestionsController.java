package org.ezlearn.controller;

import org.ezlearn.service.PurchasedCoursesService;
import org.ezlearn.service.QuestionsService;
import org.ezlearn.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/questions")
public class QuestionsController {
	
	@Autowired
	private QuestionsService questionsService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private PurchasedCoursesService purchasedCoursesService;
	
	@Autowired
	private HttpSession httpSession;
	
	@GetMapping("courses/{courseId}")
	public ResponseEntity<?> getQuestionsByCourseId(@PathVariable Long courseId) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		if(purchasedCoursesService.isPurchased(httpSession, courseId) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		
		return ResponseEntity.ok(questionsService.getQuestionsByCourseId(courseId));
	}

}
