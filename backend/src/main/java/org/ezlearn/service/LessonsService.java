package org.ezlearn.service;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.repository.LessonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonsService {
	
	@Autowired
	private LessonsRepository lessonsRepository;
	
	public List<Lessons> getLessonsByCourses(Courses courses) {
		return lessonsRepository.findByCourses(courses);
	}
	
	public Lessons getTop1LessonsByCourses(Courses courses) {
		return lessonsRepository.findTop1ByCourses(courses);
	}

}
