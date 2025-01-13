package org.ezlearn.service;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Questions;
import org.ezlearn.repository.LessonsRepository;
import org.ezlearn.repository.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionsService {
	
	@Autowired
	private QuestionsRepository questionsRepository;
	
	@Autowired
	private LessonsRepository lessonsRepository;
	
	public List<Questions> getQuestionsByCourseId(Long courseId) {
		Courses courses = new Courses();
		courses.setCourseId(courseId);
		
		List<Lessons> lessons = lessonsRepository.findByCourses(courses);
		return questionsRepository.findByLessonIn(lessons);
	}

}
