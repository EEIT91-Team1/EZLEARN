package org.ezlearn.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ezlearn.model.Users;
import org.ezlearn.model.loginresponse;
import org.ezlearn.repository.Usersrepository;
import org.ezlearn.utils.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class Usersservice {
	@Autowired
	private Usersrepository Usersrepository;
	
	public boolean adduser(Users registeruser) {
		Optional<Users> opt = Usersrepository.findByEmail(registeruser.getEmail());
		Users user = new Users();
		try {
			user = opt.get();
			return false;
		} catch (Exception e) {
			registeruser.setPassword(BCrypt.hashpw(registeruser.getPassword(), BCrypt.gensalt()));
			System.out.println(registeruser.getPassword()); 
			Usersrepository.save(registeruser);
			return true;
		}
	}
	
	public loginresponse loginuser(Users loginuser,HttpSession session) {
		Optional<Users> opt = Usersrepository.findByEmail(loginuser.getEmail());
		Users user = new Users();
		loginresponse response = new loginresponse();
		try {
			user = opt.get();
			if(!BCrypt.checkpw(loginuser.getPassword(), user.getPassword())) {
				response.setError(2);
			}else{
				response.setError(3);
				session.setAttribute("user", user);
				System.out.println(user.getEmail());
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
	
//----------------------------------------------------------------------------------	
	
	public Map<String,String> loginData(HttpSession session) {
		Users userSession = (Users)session.getAttribute("user");
		Users user=Usersrepository.findByUserId(userSession.getUserId());
		Map<String,String> data=new HashMap<String, String>();
		data.put("userId",user.getUserId()+"");
		data.put("userName",user.getUserinfo().getUserName());
		data.put("email", user.getEmail());
		String imgBase64="noImg";
		if(user.getUserinfo().getAvatar()!=null) {
		 imgBase64= "data:image/png;base64," + Base64.getEncoder().encodeToString(user.getUserinfo().getAvatar());
		}
		data.put("avatar", imgBase64);
		
		return data;
	}
	
//------------------------------------------------------------------------------------	
}
