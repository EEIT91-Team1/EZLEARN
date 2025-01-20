package org.ezlearn.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.ezlearn.DTO.LoginResponse;
import org.ezlearn.model.Users;
import org.ezlearn.service.MailService;
import org.ezlearn.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UsersController {
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	MailService mailService;
	
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
	
	@GetMapping("/getprofile")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public Users getinfo(HttpSession session) {
		Users user = usersService.getInfoFromSession(session);
		return user;
	}
	
	@GetMapping("/forget/{email}")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public boolean forget(@PathVariable String email) {
		Collection<String> receivers = Arrays.asList(email);
		String id = usersService.genresetToken(email);
		if(id != "-1") {
			mailService.sendSimpleHtml(receivers, 
			"EZLEARN 帳戶救援",
			"<p>請點擊下方的連結以恢復您的 EZLEARN 帳戶</p>"
			+ "<a href='http://127.0.0.1:5500/pages/changepw.html?id="
			+ id+"'>前往修改密碼</a>");
			return true;
		}else {
			return false;
		}
	}
	
	@PutMapping("/changepw")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public boolean changepw(@RequestBody Users user) {
		boolean n = usersService.changepw(user);
		return n;
	}
	
	@PostMapping("/changepwisExpired")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public int test1(@RequestBody Users user) {
		return usersService.tokenisExpired(user);
	}
	
	@PostMapping("/registerfromgoogle")
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	public boolean registerfromgoogle(@RequestBody Users user) {
		boolean n = usersService.adduserfromgoogle(user);	
		System.out.println(n);
		return n;
	}
	
	@PostMapping("/loginfromgoogle")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public LoginResponse loginfromgoogle(@RequestBody Users user, HttpSession session) {
		LoginResponse response = usersService.loginuserfromgoogle(user, session);
		return response;
	}

	@PostMapping("/registerfromfacebook")
	@CrossOrigin(origins = "http://127.0.0.1:5500")
	public boolean registerfromfacebook(@RequestBody Users user) {
		boolean n = usersService.adduserfromfacebook(user);	
		System.out.println(n);
		return n;
	}
	
	@PostMapping("/loginfromfacebook")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public LoginResponse loginfromfacebook(@RequestBody Users user, HttpSession session) {
		LoginResponse response = usersService.loginuserfromfacebook(user, session);
		return response;
	}
	
	@PostMapping("/mergetofacebook")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public LoginResponse mergefromgoogletofacebook(@RequestBody Users user, HttpSession session) {
		LoginResponse response = usersService.newpasswordtofacebook(user, session);
		System.out.println(response);
		return response;
	}
	
	@PostMapping("/mergetogoogle")
	@CrossOrigin(origins = "http://127.0.0.1:5500",allowCredentials = "true")
	public LoginResponse mergefromfacebooktogoogle(@RequestBody Users user, HttpSession session) {
		LoginResponse response = usersService.newpasswordtogoogle(user, session);
		System.out.println(response);
		return response;
	}
	
}
