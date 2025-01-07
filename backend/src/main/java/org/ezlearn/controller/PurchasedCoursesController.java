package org.ezlearn.controller;

import java.math.BigDecimal;
import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.Users;
import org.ezlearn.service.PurchasedCoursesService;
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
@RequestMapping("/purchased-courses")
public class PurchasedCoursesController {
	
	@Autowired
	private PurchasedCoursesService purchasedCoursesService;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/my-courses")
    public List<PurchasedCourses> getPurchasedCoursesByUser(HttpSession session) {
        Users users = (Users)session.getAttribute("user");
        return purchasedCoursesService.getPurchasedCoursesByUsers(users);
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

}
