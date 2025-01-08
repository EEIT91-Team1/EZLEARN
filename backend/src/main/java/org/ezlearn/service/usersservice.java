package org.ezlearn.service;

import java.util.Optional;

import org.ezlearn.model.Users;
import org.ezlearn.model.loginresponse;
import org.ezlearn.repository.usersrepository;
import org.ezlearn.utils.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class usersservice {
	@Autowired
	private usersrepository usersrepository;
	
	public boolean adduser(Users registeruser) {
		Optional<Users> opt = usersrepository.findByEmail(registeruser.getEmail());
		Users user = new Users();
		try {
			user = opt.get();
			return false;
		} catch (Exception e) {
			registeruser.setPassword(BCrypt.hashpw(registeruser.getPassword(), BCrypt.gensalt()));
			usersrepository.save(registeruser);
			return true;
		}
	}
	
	public loginresponse loginuser(Users loginuser,HttpSession session) {
		Optional<Users> opt = usersrepository.findByEmail(loginuser.getEmail());
		Users user = new Users();
		loginresponse response = new loginresponse();
		try {
			user = opt.get();
			if(!BCrypt.checkpw(loginuser.getPassword(), user.getPassword())) {
				response.setError(2);
			}else{
				response.setError(3);
				session.setAttribute("user", user);
			}
		} catch (Exception e) {
			response.setError(1);
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
