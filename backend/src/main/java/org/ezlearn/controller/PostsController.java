package org.ezlearn.controller;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Posts;
import org.ezlearn.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
public class PostsController {
	
	@Autowired
	private PostsService postsService;
	
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	@GetMapping("/{courseId}/posts")
	public List<Posts> getPostsByCourses(@PathVariable Long courseId) {
		Courses courses = new Courses();
		courses.setCourseId(courseId);
		return postsService.getPostsByCourses(courses);
	}

}
