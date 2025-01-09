package org.ezlearn.controller;

import org.ezlearn.model.UserInfo;
import org.ezlearn.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserInfoController {
	@Autowired
	private UserInfoService userinfoservice;
	
	@PutMapping("/updateinfo")
	@CrossOrigin(origins = "http://127.0.0.1:5500",
            allowCredentials = "true")
	public ResponseEntity<String> setprofile(@RequestBody UserInfo userinfo, HttpSession session) {
		userinfoservice.updateUserInfo(userinfo,session);
        return ResponseEntity.ok("UserInfo updated successfully");
	}
}
	
