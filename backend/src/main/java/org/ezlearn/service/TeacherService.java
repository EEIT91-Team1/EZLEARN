package org.ezlearn.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.Questions;
import org.ezlearn.model.UserInfo;
import org.ezlearn.model.Users;
import org.ezlearn.repository.Coursesrepository;
import org.ezlearn.repository.LessonsRepository;
import org.ezlearn.repository.Questionrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Service
public class TeacherService {
	@Autowired
	private Coursesrepository coursesrepository;
	@Autowired
	private Questionrepository questionrepository;
	
	public List<Map<String, Object>> findcourse(HttpSession session){
		Users user = (Users)session.getAttribute("user");
		UserInfo userinfo = new UserInfo();
		userinfo.setUserId(user.getUserId());
		List<Courses> list = coursesrepository.findByUserInfo(userinfo);
		List<Map<String, Object>> detaillist = new ArrayList<Map<String,Object>>();
		for(Courses course : list) {
			Map<String, Object> test = new HashMap<String, Object>();
			byte[] imgfile = course.getCourseImg();
			String imgbase64 = Base64.getEncoder().encodeToString(imgfile);
			test.put("courseName",course.getCourseName());
			test.put("courseImgbase64", imgbase64);
			detaillist.add(test);
		}
		for(Map<String, Object> d : detaillist) {
			System.out.printf("%s : %s\n",d.get("courseName"),d.get("courseImgbase64"));
		}
		return detaillist;
	}
	
	public List<Questions> findquest(HttpSession session){
		Users user = (Users)session.getAttribute("user");
		UserInfo userinfo = new UserInfo();
		userinfo.setUserId(user.getUserId());
		List<Courses> courselist = coursesrepository.findByUserInfo(userinfo);
		List<Questions> allquestionlist = new ArrayList<Questions>();
		for(Courses course : courselist) {
			List<Lessons> lessonlist = course.getLessonlist();
			for(Lessons lesson : lessonlist) {
				List<Questions> questionlist = questionrepository.findByLesson(lesson);
				for(Questions question : questionlist) {
					System.out.println(question.getQuestion());
					allquestionlist.add(question);
				}
			}
		}
		return allquestionlist;
	}
}
