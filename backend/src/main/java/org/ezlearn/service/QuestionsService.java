package org.ezlearn.service;

import java.util.List;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Notes;
import org.ezlearn.model.Questions;
import org.ezlearn.model.UserInfo;
import org.ezlearn.model.Users;
import org.ezlearn.repository.LessonsRepository;
import org.ezlearn.repository.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

@Service
public class QuestionsService {
	
	@Autowired
	private QuestionsRepository questionsRepository;
	
	@Autowired
	private LessonsRepository lessonsRepository;
	
	@Autowired
	private HttpSession httpSession;
	
	public List<Questions> getQuestionsByCourseId(Long courseId) {
		Courses courses = new Courses();
		courses.setCourseId(courseId);
		
		List<Lessons> lessons = lessonsRepository.findByCourses(courses);
		return questionsRepository.findByLessonIn(lessons);
	}
	
	public Questions createQuestion(Questions question) {
		Users user = (Users) httpSession.getAttribute("user");
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId(user.getUserId());
		
		question.setUserInfo(userInfo);
		
		return questionsRepository.save(question);
	}
	
	public Questions updateQuestion(Long questionId, String questionInput) {
		Questions question = questionsRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found"));
		question.setQuestionId(questionId);
		question.setQuestion(questionInput);
		
		return questionsRepository.save(question);
	}
	
	@Transactional
	public Integer deleteQuestionByQuestionId(Long questionId) {
		return questionsRepository.deleteByQuestionId(questionId);
	}
	
	public Boolean isUnauthorized(HttpSession session, Long questionId) {
		Users loginUser = (Users) session.getAttribute("user");
		Questions question = questionsRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found"));
		
		if(loginUser.getUserId() != question.getUserInfo().getUserId()) {
			return true;
		} else {
			return false;
		}
	}

}
