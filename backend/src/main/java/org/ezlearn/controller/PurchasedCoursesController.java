package org.ezlearn.controller;

import java.util.List;

import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.Users;
import org.ezlearn.service.PurchasedCoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchased-courses")
public class PurchasedCoursesController {
	
	@Autowired
	private PurchasedCoursesService purchasedCoursesService;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/{userId}")
    public List<PurchasedCourses> getPurchasedCoursesByUser(@PathVariable Long userId) {
        Users users = new Users();
        users.setUserId(userId); // 假設有 userId 的資料
        System.out.println(userId);
        return purchasedCoursesService.getPurchasedCoursesByUsers(users);
    }

}
