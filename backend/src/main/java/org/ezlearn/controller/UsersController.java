package org.ezlearn.controller;

import java.util.Map;

import org.ezlearn.DTO.LoginResponse;
import org.ezlearn.model.Users;
import org.ezlearn.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UsersController {
	
	@Autowired
	UsersService usersService;
	
	@PostMapping("/register")
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	public boolean register(@RequestBody Users user) {
		boolean n = usersService.adduser(user);	
		System.out.println(n);
		return n;
	}
	
	@PostMapping("/login")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public LoginResponse login(@RequestBody Users user, HttpSession session) {
		LoginResponse response = usersService.loginuser(user, session);
		return response;
	}
	
	@PostMapping("/logout")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public void logout(HttpSession session) {
		session.invalidate();
	}
	
	@GetMapping("/islogin")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public boolean test(HttpSession session) {
		return usersService.islogin(session);
	}
	
	@GetMapping("/logindata")
	public Map<String,String> getMethodName(HttpSession session) {
		return usersService.loginData(session);
	}

}
