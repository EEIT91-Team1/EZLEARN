package org.ezlearn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezlearn.model.Notify;
import org.ezlearn.model.NotifyList;
import org.ezlearn.model.Posts;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.Users;
import org.ezlearn.repository.CoursesRepository;
import org.ezlearn.repository.NotifyListRepository;
import org.ezlearn.repository.NotifyRepository;
import org.ezlearn.repository.PurchasedCoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class NotifyService {

	@Autowired
	private NotifyListRepository notifyListRepository;
	
	@Autowired
	private NotifyRepository notifyRepository;
	
	@Autowired
	private CoursesRepository coursesRepository;
	
	@Autowired
	private PurchasedCoursesRepository purchasedCoursesRepository;

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

	public void generatNewPostNotify(Posts post) {
		Notify notify = new Notify();
		notify.setCourses(coursesRepository.findByCourseId(post.getCourses().getCourseId()));
		notify.setNotifyContent("在「"+notify.getCourses().getCourseName()+"」有新的公告");
		notify.setCourseId(post.getCourses().getCourseId());
		notifyRepository.save(notify);
		
		List<PurchasedCourses> myCourses = purchasedCoursesRepository.findByCourses(notify.getCourses());
		for (PurchasedCourses course : myCourses) {
			NotifyList notifyList  = new NotifyList();
			notifyList.setNotify(notify);
			notifyList.setNotifyId(notify.getNotifyId());
			notifyList.setUserId(course.getUsers().getUserId());
			notifyList.setChecked(false);
			notifyListRepository.save(notifyList);
		}
	}
}