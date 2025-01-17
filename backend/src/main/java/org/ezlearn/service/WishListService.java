package org.ezlearn.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Users;
import org.ezlearn.model.WishList;
import org.ezlearn.model.WishListId;
import org.ezlearn.repository.CoursesRepository;
import org.ezlearn.repository.PurchasedCoursesRepository;
import org.ezlearn.repository.UsersRepository;
import org.ezlearn.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class WishListService {
	
	@Autowired
	private UsersRepository usersrepository;
	
	@Autowired
	private WishListRepository wishListRepository;
	
	@Autowired
	private CoursesRepository coursesrepository;
	
	@Autowired
	private PurchasedCoursesRepository purchasedCoursesRepository;
	
	public List<Map<String, String>> getWishList(HttpSession session) {
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Users userSession = (Users) session.getAttribute("user");
		if(userSession!=null) {
			List<WishList> wishLists = wishListRepository.findByUsers(userSession);
			for (WishList wishList : wishLists) {
				Map<String, String> data = new HashMap<String, String>();
				data.put("courseId", wishList.getCourses().getCourseId() + "");
				data.put("courseName", wishList.getCourses().getCourseName());
				data.put("price", wishList.getCourses().getPrice() + "");
				data.put("teacher", wishList.getCourses().getUserInfo().getUserName());
				String imgBase64 = "data:image/png;base64,"
						+ Base64.getEncoder().encodeToString(wishList.getCourses().getCourseImg());
				data.put("courseImg", imgBase64);
	
				BigDecimal rate = purchasedCoursesRepository.findAverageRateByCourseId(wishList.getCourseId());
				data.put("rate", String.format("%.1f",rate));
				dataList.add(data);
			}
		}
		return dataList;
	}

	public String add(HttpSession session, String courseId) {
		Users userSession = (Users) session.getAttribute("user");
		Users user = usersrepository.findByUserId(userSession.getUserId());
		Courses course = coursesrepository.findByCourseId(Long.parseLong(courseId));
		boolean isRepeat = false;
		List<WishList> wishLists = wishListRepository.findByUsers(userSession);
		for (WishList wishList : wishLists) {
			if (wishList.getCourseId() == Long.parseLong(courseId)) {
				isRepeat = true;
			}
		}
		if(course.getUserInfo().getUserId()==user.getUserId()) {
			return "isTeacher";
		}
		
		if (isRepeat == false) {
			WishList wishList = new WishList();
			wishList.setId(new WishListId());
			wishList.setCourseId(Long.parseLong(courseId));
			wishList.setUserId(user.getUserId());
			wishList.setCourses(course);
			wishList.setUsers(user);
			wishListRepository.save(wishList);
			return "ok";
		} else {
			return "repeat";
		}
	}
	
	public boolean delete(HttpSession session, String courseId) {
		Users userSession = (Users) session.getAttribute("user");
		Users user = usersrepository.findByUserId(userSession.getUserId());
		String userId = user.getUserId()+"";
		wishListRepository.deleteWish(userId,courseId);
		return true;
	}
	
}
