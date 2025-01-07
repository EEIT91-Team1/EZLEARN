package org.ezlearn.controller;

import java.util.List;
import java.util.Map;

import org.ezlearn.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
@RestController
@RequestMapping("/wishList")
public class WishListController {
	@Autowired
	private WishListService wishListService;
	
	@GetMapping("/get")
	public List<Map<String,String>> getWishList(HttpSession session){
		return wishListService.getWishList(session);
	}
}
