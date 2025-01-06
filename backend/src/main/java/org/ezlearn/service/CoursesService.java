package org.ezlearn.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.ezlearn.model.Courses;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.UserInfo;
import org.ezlearn.model.Users;
import org.ezlearn.repository.Coursesrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Service
public class CoursesService {
	@Autowired
	private Coursesrepository coursesrepository;
	
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
}
