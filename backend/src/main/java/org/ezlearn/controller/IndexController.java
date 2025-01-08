package org.ezlearn.controller;

import java.util.List;
import java.util.Map;

import org.ezlearn.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController {
	
	@Autowired
	private IndexService indexService;

	@GetMapping("/courses")
	public ResponseEntity<List<Map<String, String>>> searchCourse() {
		return ResponseEntity.ok(indexService.searchCourse());
	}

	@GetMapping("/review")
	public ResponseEntity<List<Map<String, String>>> review() {
		return ResponseEntity.ok(indexService.review());
	}

}
