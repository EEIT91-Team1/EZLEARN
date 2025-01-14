package org.ezlearn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezlearn.model.Courses;
import org.ezlearn.model.UserInfo;
import org.ezlearn.repository.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoursesService {
	
	@Autowired
	private CoursesRepository coursesRepository;
	
	public List<Courses> getCoursesByUsers(UserInfo userInfo) {
		return coursesRepository.findByUserInfo(userInfo);
	}
	
	public Courses getCoursesByCourseId(Long courseId) {
		return coursesRepository.findByCourseId(courseId);
	}

	public List<Map<String,String>> getAllNameId(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		List<Courses> res = coursesRepository.findAll();
		for(Courses course : res) {
			Map<String,String> map = new HashMap<>();
			map.put("Name",course.getCourseName());
			map.put("Id",course.getCourseId()+"");
			list.add(map);
		}
		return list;
	}
}
