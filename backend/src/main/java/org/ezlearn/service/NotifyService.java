package org.ezlearn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezlearn.model.NotifyList;
import org.ezlearn.model.Users;
import org.ezlearn.repository.NotifyListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class NotifyService {

	@Autowired
	private NotifyListRepository notifyListRepository;

	public List<Map<String, String>> get(HttpSession session) {
		Users user = (Users) session.getAttribute("user");
		List<NotifyList> list = notifyListRepository.findByUserId(user.getUserId());
		List<Map<String, String>> listmap = new ArrayList<Map<String, String>>();
		for (NotifyList notifyList : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("content", notifyList.getNotify().getNotifyContent());
			map.put("checked", notifyList.isChecked() + "");
			map.put("time", notifyList.getCreatedAt());
			listmap.addFirst(map);
		}
		return listmap;
	}

	public int checked(HttpSession session) {
		Users user = (Users) session.getAttribute("user");
		return notifyListRepository.checked(user.getUserId());
		
	}
}
