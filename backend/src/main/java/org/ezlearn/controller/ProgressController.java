package org.ezlearn.controller;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Progress;
import org.ezlearn.model.ProgressId;
import org.ezlearn.model.Users;
import org.ezlearn.repository.CoursesRepository;
import org.ezlearn.repository.LessonsRepository;
import org.ezlearn.service.ProgressService;
import org.ezlearn.service.PurchasedCoursesService;
import org.ezlearn.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/progress")
public class ProgressController {
	
	@Autowired
	private ProgressService progressService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private PurchasedCoursesService purchasedCoursesService;
	
	@Autowired
	private LessonsRepository lessonsRepository;
	
	@Autowired
	private CoursesRepository coursesRepository;
	
	@Autowired
	private HttpSession httpSession;
	
	@GetMapping("lesson/{lessonId}")
	public ResponseEntity<?> getProgressByUsersAndLessons(@PathVariable Long lessonId) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		Users user = (Users) httpSession.getAttribute("user");
		Lessons lesson = new Lessons();
		lesson.setLessonId(lessonId);
		return ResponseEntity.ok(progressService.getProgressByUsersAndLessons(user, lesson));
	}
	
	@GetMapping("/courses/{courseId}")
	public ResponseEntity<?> getProgressByUsersAndCourses(@PathVariable Long courseId) { //取得該課程所有章節進度
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}
		
		Users user = (Users) httpSession.getAttribute("user");
		Courses course = coursesRepository.findByCourseId(courseId);
		return ResponseEntity.ok(progressService.getProgressByUsersAndCourses(user, course));
	}
	
	@PutMapping("lesson/{lessonId}")
	public ResponseEntity<?> updateProgress(@PathVariable Long lessonId, @RequestBody Progress progress) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}

	    Users user = (Users) httpSession.getAttribute("user");
	    Lessons lesson = lessonsRepository.findById(lessonId)
	    	    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));
	    
	    ProgressId progressId = new ProgressId(user.getUserId(), lessonId);
	    progress.setProgressId(progressId);
	    progress.setUsers(user);
	    progress.setLessons(lesson);
	    progress.setProgressPercent((progress.getProgressTime() * 100) / progress.getTotalDuration());
	    
        return ResponseEntity.ok(progressService.saveProgress(progress));
	}
	
	@PostMapping("lesson/{lessonId}")
	public ResponseEntity<?> createProgress(@PathVariable Long lessonId, @RequestBody Progress progress) {
		if (usersService.islogin(httpSession) == false) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthenticated");
		}

	    Users user = (Users) httpSession.getAttribute("user");
	    Lessons lesson = lessonsRepository.findById(lessonId)
	    	    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));
	    
	    ProgressId progressId = new ProgressId(user.getUserId(), lessonId);
	    progress.setProgressId(progressId);
	    progress.setUsers(user);
	    progress.setLessons(lesson);
	    progress.setProgressPercent((progress.getProgressTime() * 100) / progress.getTotalDuration());
	    
        return ResponseEntity.ok(progressService.saveProgress(progress));
	}
	
	@GetMapping("courses/{courseId}/last-view")
	public ResponseEntity<?> getLastView(@PathVariable Long courseId) { //取得該課程最後觀看的章節進度
		Users user = (Users) httpSession.getAttribute("user");
		Courses courses = coursesRepository.findByCourseId(courseId);
		return ResponseEntity.ok(progressService.getLastView(user, courses));
	}

}
