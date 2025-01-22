package org.ezlearn.controller;

import java.math.BigDecimal;
import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.Users;
import org.ezlearn.service.PurchasedCoursesService;
import org.ezlearn.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/purchased-courses")
public class PurchasedCoursesController {
	
	@Autowired
	private PurchasedCoursesService purchasedCoursesService;
	
	@Autowired
	private UsersService usersService;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/my-courses")
    public List<PurchasedCourses> getPurchasedCoursesByUser(HttpSession session) {
        Users users = (Users)session.getAttribute("user");
        return purchasedCoursesService.getPurchasedCoursesByUsers(users);
    }
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/{courseId}")
    public List<PurchasedCourses> getPurchasedCoursesByCourseId(@PathVariable Long courseId) {
        Courses courses = new Courses();
        courses.setCourseId(courseId);
        return purchasedCoursesService.getPurchasedCoursesByCourses(courses);
    }
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@PutMapping("/{courseId}/review")
    public ResponseEntity<?> updateCourseReview(@PathVariable Long courseId, HttpSession session, @RequestBody PurchasedCourses purchasedCourses) {
		if (usersService.islogin(session) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		if(purchasedCoursesService.isPurchased(session, courseId) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}
		
		Courses courses = new Courses();
        courses.setCourseId(courseId);
		Users users = (Users)session.getAttribute("user");
        return ResponseEntity.ok(purchasedCoursesService.updateCourseReview(courses, users, purchasedCourses.getCourseRate(), purchasedCourses.getCourseReview()));
    }
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
    @GetMapping("/{courseId}/average-rate")
    public ResponseEntity<BigDecimal> getAverageRate(@PathVariable Long courseId) {
		BigDecimal averageRate = purchasedCoursesService.getAverageRateForCourse(courseId);
        if (averageRate != null) {
            return ResponseEntity.ok(averageRate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
	
	 @GetMapping("/isPurchased/{courseId}")
	 public boolean isPurchased(HttpSession session,@PathVariable Long courseId) {
		 Users users = (Users)session.getAttribute("user");
		 return purchasedCoursesService.isPurchased(session,courseId);
	 }
}
