package org.ezlearn.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ezlearn.service.PostCoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/check")
public class CheckLoadController {
	
	@Autowired
	private PostCoursesService postCoursesService;
	
	
	public CheckLoadController(PostCoursesService postCoursesService) {
		this.postCoursesService=postCoursesService;
	}
	
	
	
	@GetMapping("/allParameters")
	public ResponseEntity<Map<String, String>> checkAllParameters(){
		System.out.println("courseName: " + postCoursesService.getCourseName());
		System.out.println("courseClass: " + postCoursesService.getCourseClasse());
		System.out.println("coursePrice: " + postCoursesService.getCoursePrice());
		System.out.println("courseIntro: " + postCoursesService.getCourseIntro());
		System.out.println("courseSummary: " + postCoursesService.getCourseSummary());
		System.out.println("courseImage: " + postCoursesService.getCourseImages());

		Map<String, String> result = new HashMap<>();
		
		
		result.put("courseName", StringUtils.isNotEmpty(postCoursesService.getCourseName()) ? "有值" : "無值");
	    result.put("courseClass", StringUtils.isNotEmpty(postCoursesService.getCourseClasse()) ? "有值" : "無值");
	    result.put("coursePrice", postCoursesService.getCoursePrice() != null ? "有值" : "無值");
	    result.put("courseIntro", StringUtils.isNotEmpty(postCoursesService.getCourseIntro()) ? "有值" : "無值");
	    result.put("courseSummary", StringUtils.isNotEmpty(postCoursesService.getCourseSummary()) ? "有值" : "無值");

	    // 處理 List 判斷
	    result.put("courseImage", (postCoursesService.getCourseImages() != null && !postCoursesService.getCourseImages().isEmpty()) ? "有值" : "無值");
		
		return ResponseEntity.ok(result);
		
	}
}
