package org.ezlearn.controller;

import java.util.List;
import java.util.Map;

import org.ezlearn.repository.NotifyListRepository;
import org.ezlearn.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/notify")
public class NotifyController {
	@Autowired
	NotifyService notifyService;

	@GetMapping("/get")
	public List<Map<String, String>> get(HttpSession session) {
		return notifyService.get(session);
	}

	@GetMapping("/checked")
	public int checked(HttpSession session) {
		return notifyService.checked(session);
	}
}
