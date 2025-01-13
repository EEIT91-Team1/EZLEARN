package org.ezlearn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezlearn.model.NotifyList;
import org.ezlearn.model.Users;
import org.ezlearn.repository.NotifyListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class NotifyService {

	@Autowired
	private NotifyListRepository notifyListRepository;

	public List<Map<String, String>> get(HttpSession session) {
		Users user = (Users) session.getAttribute("user");
		List<NotifyList> list = notifyListRepository.findByUserId(user.getUserId());
		List<Map<String, String>> listmap = new ArrayList<Map<String, String>>();
		for (NotifyList notifyList : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("content", notifyList.getNotify().getNotifyContent());
			map.put("checked", notifyList.isChecked() + "");
			map.put("time", notifyList.getCreatedAt());			
			map.put("courseName",notifyList.getNotify().getCourses().getCourseName());
			map.put("courseId",notifyList.getNotify().getCourseId()+"");
			map.put("notifyId",notifyList.getNotifyId()+"");	
			listmap.addFirst(map);
		}
		return listmap;
	}

	public int checkedAll(HttpSession session) {
		Users user = (Users) session.getAttribute("user");
		return notifyListRepository.checkedAll(user.getUserId());
	}
	
	public int checkedNotify(HttpSession session,String notifyId) {
		Users user = (Users) session.getAttribute("user");
		return notifyListRepository.checkedNotify(user.getUserId(),Long.parseLong(notifyId));
	}

	public int checkedCourse(HttpSession session,String courseId) {
		Users user = (Users) session.getAttribute("user");
		return notifyListRepository.checkedCourse(user.getUserId(),Long.parseLong(courseId));
	}
	
	public int deleteAll(HttpSession session) {
		Users user = (Users) session.getAttribute("user");
		return notifyListRepository.deleteAll(user.getUserId());
	}
	
	public int deleteNotify(HttpSession session,String notifyId) {
		Users user = (Users) session.getAttribute("user");
		return notifyListRepository.deleteNotify(user.getUserId(),Long.parseLong(notifyId));
	}
	
	public int deleteCourse(HttpSession session,String courseId) {
		Users user = (Users) session.getAttribute("user");
		return notifyListRepository.deleteCourse(user.getUserId(),Long.parseLong(courseId));
	}
}