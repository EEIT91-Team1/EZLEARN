package org.ezlearn.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

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
		String randomString = generateRandomString(10); // 生成10碼的隨機字串
		String randomString2 = generateRandomString(10);
		registerUser.setPassword(BCrypt.hashpw(registerUser.getPassword(), BCrypt.gensalt()));
		registerUser.setGooglePassword(BCrypt.hashpw(randomString, BCrypt.gensalt()));
		registerUser.setFacebookPassword(BCrypt.hashpw(randomString2, BCrypt.gensalt()));
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
	
	public Map<String,String> loginData(HttpSession session) {
		Users userSession = (Users)session.getAttribute("user");
		Users user = usersRepository.findByUserId(userSession.getUserId());
		Map<String,String> data = new HashMap<String, String>();
		data.put("userId",user.getUserId()+"");
		data.put("userName",user.getUserInfo().getUserName());
		data.put("email", user.getEmail());
		String imgBase64 = "noImg";
		if(user.getUserInfo().getAvatar()!=null &&!(Base64.getEncoder().encodeToString(user.getUserInfo().getAvatar())).isEmpty()) {
		 imgBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(user.getUserInfo().getAvatar());
		}

		data.put("avatar", imgBase64);
		
		return data;
	}
	
	public Users getInfoFromSession(HttpSession session) {
		Users loginuser = (Users)session.getAttribute("user");
		Optional<Users> opt = usersRepository.findByEmail((String)loginuser.getEmail());
		Users user = new Users();
		try {
			user = opt.get();
			user.setPassword("");
			return user;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String genresetToken(String email) {
		Optional<Users> opt = usersRepository.findByEmail(email);
		Users user = new Users();
		try {
			user = opt.get();
			String resetToken = (BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
			user.setResetToken(resetToken);
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime tenMinutesLater = now.plusMinutes(10);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedTenMinutesLater = tenMinutesLater.format(formatter);
			user.setResetTokenExpiry(formattedTenMinutesLater);
			usersRepository.save(user);
			return resetToken;
		} catch (Exception e) {
			return "-1";
		}
	}
	
	public boolean changepw(Users changeuser) {
		Users user1 = usersRepository.findByresetToken(changeuser.getResetToken());
		if(user1 != null) {
			user1.setPassword(BCrypt.hashpw(changeuser.getPassword(), BCrypt.gensalt()));
			user1.setResetToken(null);
			user1.setResetTokenExpiry(null);
			usersRepository.save(user1);
			return true;   
		}else{
			return false;
		}
	}
	
	public int tokenisExpired(Users user) {
		//1:成功 2:網頁不存在 3:網頁已過期 
		Users user1 = usersRepository.findByresetToken(user.getResetToken());
		if(user1 != null) {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime tenMinutesLater = LocalDateTime.parse(user1.getResetTokenExpiry(), formatter);
			 if (now.isAfter(tenMinutesLater)) {
				 return 3;
		     } else {
		         return 1;
		     }
		}else {
			return 2;
		}
	}
	public boolean adduserfromgoogle(Users registerUser) {
		Optional<Users> opt = usersRepository.findByEmail(registerUser.getEmail());
		if (opt.isPresent()) {
			return false;
		}
		String randomString = generateRandomString(10); // 生成10碼的隨機字串
		String randomString2 = generateRandomString(10); 
		registerUser.setPassword(BCrypt.hashpw(randomString, BCrypt.gensalt()));
		registerUser.setFacebookPassword(BCrypt.hashpw(randomString2, BCrypt.gensalt()));
		registerUser.setGooglePassword(BCrypt.hashpw(registerUser.getGooglePassword(), BCrypt.gensalt()));
		System.out.println(registerUser.getPassword()); 
		usersRepository.save(registerUser);
		return true;
	}
	
	public LoginResponse loginuserfromgoogle(Users loginUser,HttpSession session) {
		Optional<Users> opt = usersRepository.findByEmail(loginUser.getEmail());
		Users user = new Users();
		LoginResponse response = new LoginResponse();
		try {
			user = opt.get();
			if(!BCrypt.checkpw(loginUser.getGooglePassword(), user.getGooglePassword())) {
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
	
	public boolean adduserfromfacebook(Users registerUser) {
		Optional<Users> opt = usersRepository.findByEmail(registerUser.getEmail());
		if (opt.isPresent()) {
			return false;
		}
		String randomString = generateRandomString(10); // 生成10碼的隨機字串
		String randomString2 = generateRandomString(10); 
		registerUser.setPassword(BCrypt.hashpw(randomString, BCrypt.gensalt()));
		registerUser.setGooglePassword(BCrypt.hashpw(randomString2, BCrypt.gensalt()));
		registerUser.setFacebookPassword(BCrypt.hashpw(registerUser.getFacebookPassword(), BCrypt.gensalt()));
		System.out.println(registerUser.getPassword()); 
		usersRepository.save(registerUser);
		return true;
	}
	
	public LoginResponse loginuserfromfacebook(Users loginUser,HttpSession session) {
		Optional<Users> opt = usersRepository.findByEmail(loginUser.getEmail());
		Users user = new Users();
		LoginResponse response = new LoginResponse();
		try {
			user = opt.get();
			if(!BCrypt.checkpw(loginUser.getFacebookPassword(), user.getFacebookPassword())) {
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
	
	public LoginResponse newpasswordtogoogle(Users loginUser,HttpSession session) {
		Optional<Users> opt = usersRepository.findByEmail(loginUser.getEmail());
		Users user = new Users();
		LoginResponse response = new LoginResponse();
		try {
			user = opt.get();
			if(!BCrypt.checkpw(loginUser.getGooglePassword(), user.getGooglePassword())) {
				response.setError(2);
				response.setMsg("set GooglePassword success");
				loginUser.setGooglePassword(BCrypt.hashpw(loginUser.getGooglePassword(), BCrypt.gensalt()));
				session.setAttribute("user", user);
				System.out.println(user.getEmail());
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
	
	public LoginResponse newpasswordtofacebook(Users loginUser,HttpSession session) {
		Optional<Users> opt = usersRepository.findByEmail(loginUser.getEmail());
		Users user = new Users();
		LoginResponse response = new LoginResponse();
		try {
			user = opt.get();
			if(!BCrypt.checkpw(loginUser.getFacebookPassword(), user.getFacebookPassword())) {
				response.setError(2);
				response.setMsg("set GooglePassword success");
				loginUser.setFacebookPassword(BCrypt.hashpw(loginUser.getFacebookPassword(), BCrypt.gensalt()));
				session.setAttribute("user", user);
				System.out.println(user.getEmail());
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
	
	private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }
}
