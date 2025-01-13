package org.ezlearn.service;

import org.ezlearn.model.UserInfo;
import org.ezlearn.model.Users;
import org.ezlearn.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
@Service
public class UserInfoService {
	@Autowired
	private UserInfoRepository userinforepository;
	
	public void updateUserInfo(UserInfo newuserinfo, HttpSession session) {
		Users loginuser = (Users)session.getAttribute("user");
		System.out.println(loginuser.getUserId());
        UserInfo userInfo = userinforepository.findById(loginuser.getUserId())
                .orElseThrow(() -> new RuntimeException("UserInfo not found for userId: " + loginuser.getUserId()));

        userInfo.setAvatar(newuserinfo.getAvatar());
        userInfo.setUserName(newuserinfo.getUserName());
        userInfo.setBirthday(newuserinfo.getBirthday());
        userInfo.setGender(newuserinfo.getGender());
        userInfo.setPhone(newuserinfo.getPhone());
        userInfo.setUserIntro(newuserinfo.getUserIntro());

        userinforepository.save(userInfo);
    }
}
	
	
