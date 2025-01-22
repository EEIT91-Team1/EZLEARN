package org.ezlearn.controller;

import java.util.List;
import java.util.Map;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.ezlearn.model.Courses;
import org.ezlearn.model.Posts;
import org.ezlearn.model.Questions;
import org.ezlearn.repository.CoursesRepository;
import org.ezlearn.service.NotifyService;
import org.ezlearn.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
	@Autowired
	private TeacherService teacherservice;
	
	@Autowired
	private NotifyService notifyService;
	
	@GetMapping("/teachercourse")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public List<Map<String, Object>> teachercourse(HttpSession session) {
		List<Map<String, Object>> coursenamelist = teacherservice.findcourse(session);
		return coursenamelist;
	}
	
	@GetMapping("/teacherquestion")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public List<Questions> teacherquestion(HttpSession session) {
		List<Questions> questionlist = teacherservice.findquest(session);
		return questionlist;
	}
	
	@PutMapping("/teacheranswer")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public void teacheranswer(@RequestBody Questions question) {
		teacherservice.updateanswer(question);
	}
	
	@PostMapping("/teachersendpost")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public boolean teachersnedpost(@RequestBody Posts post) {
		boolean errorcode = teacherservice.sendpost(post);
		notifyService.generatNewPostNotify(post);
		return errorcode;
	}
	
	@GetMapping("/teachergetpost/{courseid}")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public List<Posts> teachergetpost(@PathVariable Long courseid) {
		List<Posts> postlist = teacherservice.getpost(courseid);
		return postlist;
	}
	
	@PutMapping("/editcourse")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public void editcourse(@RequestBody Courses course) {
		teacherservice.editcoursedetail(course);
	}
}
