package org.ezlearn.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.Posts;
import org.ezlearn.model.Questions;
import org.ezlearn.model.UserInfo;
import org.ezlearn.model.Users;
import org.ezlearn.repository.CoursesRepository;
import org.ezlearn.repository.LessonsRepository;
import org.ezlearn.repository.PostsRepository;
import org.ezlearn.repository.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class TeacherService {
	
	@Autowired
	private CoursesRepository coursesrepository;
	
	@Autowired
	private QuestionsRepository questionrepository;
	
	@Autowired
	private PostsRepository postsRepository;
	
	@Autowired
	private LessonsRepository lessonsRepository;
	
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
			test.put("price",course.getPrice());
			test.put("courseSummary",course.getCourseSummary());
			test.put("courseIntro",course.getCourseIntro());
			test.put("courseType",course.getCourseType());
			test.put("courseImgbase64", imgbase64);
			test.put("courseid",course.getCourseId());
			detaillist.add(test);
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
			List<Lessons> lessonlist = lessonsRepository.findByCourses(course);
			for(Lessons lesson : lessonlist) {
				List<Questions> questionlist = questionrepository.findByLesson(lesson);
				for(Questions question : questionlist) {
					allquestionlist.add(question);
				}
			}
		}
		for(Questions question: allquestionlist) {
			System.out.println(question.getUserInfo().getUserIntro());
		}
		return allquestionlist;
	}
	
	public void updateanswer(Questions question) {
		Optional<Questions> opt = questionrepository.findById(question.getQuestionId());
		Questions updatequestion = opt.get();
		updatequestion.setAnswer(question.getAnswer());
		questionrepository.save(updatequestion);
	}
	
	public boolean sendpost(Posts post) {		
		Posts insertpost = postsRepository.save(post);
		if(insertpost != null) {
			return true;
		}else {
			return false;
		}
	}
	
	public List<Posts> getpost(Long courseid) {
		Courses course = new Courses();
		course.setCourseId(courseid);
		List<Posts> postlist = postsRepository.findByCourses(course);
		return postlist;
	}
	
	public void editcoursedetail(Courses course) {
		Courses editcourse = coursesrepository.findByCourseId(course.getCourseId());
		editcourse.setCourseName(course.getCourseName());
		editcourse.setPrice(course.getPrice());
		editcourse.setCourseType(course.getCourseType());
		editcourse.setCourseSummary(course.getCourseSummary());
		editcourse.setCourseIntro(course.getCourseIntro());
		editcourse.setCourseImg(course.getCourseImg());
		coursesrepository.save(editcourse);
	}
}
