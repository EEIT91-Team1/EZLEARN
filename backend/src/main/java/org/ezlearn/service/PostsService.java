package org.ezlearn.service;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Posts;
import org.ezlearn.repository.PostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostsService {
	
	@Autowired
	private PostsRepository postsRepository;
	
	public List<Posts> getPostsByCourses(Courses courses) {
		return postsRepository.findByCourses(courses);
	}

}
