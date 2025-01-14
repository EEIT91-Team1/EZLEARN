package org.ezlearn.controller;

import java.util.List;
import java.util.Map;

import org.ezlearn.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/notify")
public class NotifyController {
	@Autowired
	NotifyService notifyService;

	@GetMapping("/get")
	public List<Map<String, String>> get(HttpSession session) {
		return notifyService.get(session);
	}

	@PutMapping("/checkedAll")
	public int checked(HttpSession session) {
		return notifyService.checkedAll(session);
	}
	@PutMapping("/checkedNotify")
	public int checkedNotify(HttpSession session,@RequestParam String notifyId) {
		return notifyService.checkedNotify(session,notifyId);
	}
	@PutMapping("/checkedCourse")
	public int checkedCourse(HttpSession session,@RequestParam String courseId) {
		return notifyService.checkedCourse(session,courseId);
	}
	
	@DeleteMapping("/deleteAll")
	public int delete(HttpSession session) {
		return notifyService.deleteAll(session);
	}
	@DeleteMapping("/deleteNotify")
	public int deleteNotify(HttpSession session,@RequestParam String notifyId) {
		return notifyService.deleteNotify(session,notifyId);
	}
	@DeleteMapping("/deleteCourse")
	public int deleteCourse(HttpSession session,@RequestParam String courseId) {
		return notifyService.deleteCourse(session,courseId);
	}
}
