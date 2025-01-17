package org.ezlearn.controller;

import java.util.List;
import java.util.Map;

import org.ezlearn.model.WishList;
import org.ezlearn.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/wishList")
public class WishListController {
	@Autowired
	private WishListService wishListService;

	@GetMapping("/get")
	public Object getWishList(HttpSession session) {

		List<Map<String, String>> list = wishListService.getWishList(session);
		if (list.size() != 0) {
			return list;
		} else {
			return "0";
		}
	}

	@PostMapping("/add")
	public String add(HttpSession session, @RequestParam String courseId) {
		return wishListService.add(session, courseId);
	}

	@PostMapping("/delete")
	public boolean del(HttpSession session, @RequestParam String courseId) {
		return wishListService.delete(session, courseId);

	}

}
