package org.ezlearn.controller;

import java.util.List;
import java.util.Map;

import org.ezlearn.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/teacher")
public class teachercontroller {
	@Autowired
	private CoursesService teacherservice;
	
	@GetMapping("/teachercourse")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public List<Map<String, Object>> teachercourse(HttpSession session) {
		List<Map<String, Object>> coursenamelist = teacherservice.findcourse(session);
		return coursenamelist;
	}
}
