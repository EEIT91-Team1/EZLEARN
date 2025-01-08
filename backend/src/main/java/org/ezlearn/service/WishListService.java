package org.ezlearn.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezlearn.model.Courses;
import org.ezlearn.model.PurchasedCourses;
import org.ezlearn.model.Users;
import org.ezlearn.model.WishList;
import org.ezlearn.model.WishListId;
import org.ezlearn.repository.Coursesrepository;
import org.ezlearn.repository.WishListRepository;
import org.ezlearn.repository.Usersrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpSession;

@Service
public class WishListService {
	@Autowired
	private Usersrepository usersrepository;
	@Autowired
	private WishListRepository wishListRepository;
	@Autowired
	private Coursesrepository coursesrepository;
	
	public List<Map<String, String>> getWishList(HttpSession session) {
		Users userSession = (Users) session.getAttribute("user");
		Users user = usersrepository.findByUserId(userSession.getUserId());
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		List<WishList> wishLists = user.getWishList();
		for (WishList wishList : wishLists) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("courseId", wishList.getCourses().getCourseId() + "");
			data.put("courseName", wishList.getCourses().getCourseName());
			data.put("price", wishList.getCourses().getPrice() + "");
			data.put("teacher", wishList.getCourses().getUsers().getUserinfo().getUserName());
			String imgBase64 = "data:image/png;base64,"
					+ Base64.getEncoder().encodeToString(wishList.getCourses().getCourseImg());
			data.put("courseImg", imgBase64);
			List<PurchasedCourses> coursePurchased = wishList.getCourses().getPurchasedCourses();
			Integer rate = 0;
			Double rates = 0.0;
			Integer student = 0;
			for (PurchasedCourses p : coursePurchased) {
				if (p.getCourseRate() != null) {
					rate += p.getCourseRate();
					student += 1;
				}
			}
			rates = (double) rate / student;
			data.put("rate", String.format("%.1f",rates));
			dataList.add(data);
		}
		System.out.println(dataList.size());
		return dataList;
	}

	public boolean add(HttpSession session, String courseId) {
		Users userSession = (Users) session.getAttribute("user");
		Users user = usersrepository.findByUserId(userSession.getUserId());
		Courses course = coursesrepository.findByCourseId(Long.parseLong(courseId));
		boolean isRepeat = false;
		List<WishList> wishLists = user.getWishList();
		for (WishList wishList : wishLists) {
			if (wishList.getCourseId() == Long.parseLong(courseId)) {
				System.out.println(111);
				isRepeat = true;
			}
		}
		if (isRepeat == false) {
			WishList wishList = new WishList();
			wishList.setId(new WishListId());
			wishList.setCourseId(Long.parseLong(courseId));
			wishList.setUserId(user.getUserId());
			wishList.setCourses(course);
			wishList.setUsers(user);
			wishListRepository.save(wishList);
//			wishListRepository.addWishList(user.getUserId() + "", courseId);
			return true;
		} else {
			return false;
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
