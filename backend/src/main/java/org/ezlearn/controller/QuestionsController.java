package org.ezlearn.controller;

import org.ezlearn.model.Questions;
import org.ezlearn.service.PurchasedCoursesService;
import org.ezlearn.service.QuestionsService;
import org.ezlearn.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@GetMapping("teacher/{courseId}")
	public ResponseEntity<?> getByCourseId(@PathVariable Long courseId) {
		
		return ResponseEntity.ok(questionsService.getQuestionsByCourseId(courseId));
	}
	
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
	
	@PostMapping("/create")
	public ResponseEntity<?> createQuestion(@RequestBody Questions question) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		return ResponseEntity.ok(questionsService.createQuestion(question));
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateQuestion(@RequestBody Questions question) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		if (questionsService.isUnauthorized(httpSession, question.getQuestionId()) == true) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		
		return ResponseEntity.ok(questionsService.updateQuestion(question.getQuestionId(), question.getQuestion()));
	}
	
	@DeleteMapping("/{questionId}/delete")
	public ResponseEntity<?> deleteQuestionByQuestionId(@PathVariable Long questionId) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		if (questionsService.isUnauthorized(httpSession, questionId) == true) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		
		return ResponseEntity.ok(questionsService.deleteQuestionByQuestionId(questionId));
	}

}
