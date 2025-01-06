package org.ezlearn.service;

import java.util.Optional;

import org.ezlearn.DTO.LoginResponse;
import org.ezlearn.model.Users;
import org.ezlearn.repository.UsersRepository;
import org.ezlearn.utils.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;
	
	public boolean adduser(Users registerUser) {
		Optional<Users> opt = usersRepository.findByEmail(registerUser.getEmail());
		if (opt.isPresent()) {
			return false;
		}
		
		registerUser.setPassword(BCrypt.hashpw(registerUser.getPassword(), BCrypt.gensalt()));
		System.out.println(registerUser.getPassword()); 
		usersRepository.save(registerUser);
		return true;
	}
	
	public LoginResponse loginuser(Users loginUser,HttpSession session) {
		Optional<Users> opt = usersRepository.findByEmail(loginUser.getEmail());
		Users user = new Users();
		LoginResponse response = new LoginResponse();
		try {
			user = opt.get();
			if(!BCrypt.checkpw(loginUser.getPassword(), user.getPassword())) {
				response.setError(2);
				response.setMsg("login failure");
			}else{
				response.setError(3);
				response.setMsg("login success");
				session.setAttribute("user", user);
				System.out.println(user.getEmail());
			}
		} catch (Exception e) {
			response.setError(1);
			response.setMsg("account not found");
		}
		return response;
	}
	
	public boolean islogin(HttpSession session) {
		Users user = (Users)session.getAttribute("user");
		if(user == null) {
			return false;
		}else {
			return true;
		}
	}
	
}
