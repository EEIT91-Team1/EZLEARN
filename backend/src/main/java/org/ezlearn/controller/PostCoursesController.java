package org.ezlearn.controller;

import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ezlearn.model.CourseInformation;
import org.ezlearn.model.Users;
import org.ezlearn.service.PostCoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@RestController
@RequestMapping("/orders10")
public class PostCoursesController {
	@Autowired
	private PostCoursesService postCoursesService;

//儲存課程名稱至伺服器
	@PostMapping("/test10")
	public ResponseEntity<String> saveCourseName(@RequestBody CourseInformation courseInformation) {
		String coursename = courseInformation.getCoursename();
		postCoursesService.saveCourseName(coursename); // 呼叫 Service 處理邏輯
		return ResponseEntity.ok("課程名稱已成功暫存：" + coursename);
	}

//將課程名稱清空
	@PostMapping("/refreshCourseName")
	public ResponseEntity<String> refreshCourseName() {
		postCoursesService.refreshCourseName();
		return ResponseEntity.ok("課程名稱已成功清除：");
	}

//讀取課程名稱回網頁
	@PostMapping("/test11")
	public ResponseEntity<Map<String, Object>> loadCourseName() {
		Map<String, Object> response = new HashMap<>();
		// response.put("message", "課程名稱已成功暫存");
		String courseName = postCoursesService.getCourseName();
		if (courseName != null && !courseName.isEmpty()) {
			response.put("data", courseName);
		} else {
			response.put("data", null); // 空資料
		}
		return ResponseEntity.ok(response); // 返回 200 OK，資料即使為空
	}

//儲存課程主題至伺服器
	@PostMapping("/test13")
	public ResponseEntity<String> saveCourseClass(@RequestBody CourseInformation courseInformation) {
		String courseClass = courseInformation.getCourseClass();
		postCoursesService.saveCourseClasse(courseClass); // 呼叫 Service 處理邏輯
		return ResponseEntity.ok("課程名稱已成功暫存：" + courseClass);
	}

//將課程主題清空
	@PostMapping("/refreshCourseClass")
	public ResponseEntity<String> refreshCourseClass() {
		postCoursesService.refreshCourseClasse();
		return ResponseEntity.ok("課程主題已成功清除：");
	}

//讀取課程主題回網頁
	@PostMapping("/test14")
	public ResponseEntity<Map<String, Object>> loadCourseClass() {
		Map<String, Object> response = new HashMap<>();
		String courseClass = postCoursesService.getCourseClasse();
		if (courseClass != null && !courseClass.isEmpty()) {
			response.put("data", courseClass);
		} else {
			response.put("data", null); // 空資料
		}
		return ResponseEntity.ok(response); // 返回 200 OK，資料即使為空
	}

//將課程價格暫存	
	@PostMapping("/savecourseprice")
	public ResponseEntity<String> saveCoursePrice(@RequestBody CourseInformation courseInformation) {
		Integer courseprice = courseInformation.getCoursePrice();
		postCoursesService.saveCoursePrice(courseprice); // 呼叫 Service 處理邏輯
		return ResponseEntity.ok("課程名稱已成功暫存：" + courseprice);
	}

//將課程價格清空
	@PostMapping("/refreshCoursePrice")
	public ResponseEntity<String> refreshCoursePrice() {
		postCoursesService.refreshCoursePrice();
		return ResponseEntity.ok("課程價格已成功清除：");
	}

//讀取課程價格回網頁	
	@PostMapping("/loadcourseprice")
	public ResponseEntity<Map<String, Object>> loadCoursePrice() {
		Map<String, Object> response = new HashMap<>();
		Integer coursePrice = postCoursesService.getCoursePrice();
		if (coursePrice != null) {
			response.put("data", coursePrice);
		} else {
			response.put("data", null); // 空資料
		}
		return ResponseEntity.ok(response); // 返回 200 OK，資料即使為空
	}
//課程介紹暫存
	@PostMapping("/test20")
	public ResponseEntity<String> saveCourseIntro(@RequestBody CourseInformation courseInformation) {
		String courseIntro = courseInformation.getCourseIntro();
		postCoursesService.saveCourseIntro(courseIntro); // 呼叫 Service 處理邏輯
		return ResponseEntity.ok("課程名稱已成功暫存：" + courseIntro);
	}
//課程介紹清空
	@PostMapping("refreshCourseIntro")
	public ResponseEntity<String> refreshCourseIntro() {
		postCoursesService.refreshCourseIntro();
		return ResponseEntity.ok("課程介紹已成功清除：");
	}

//讀取課程介紹回網頁
	@PostMapping("/test21")
	public ResponseEntity<Map<String, Object>> loadCourseIntro() {
		Map<String, Object> response = new HashMap<>();
		String courseIntro = postCoursesService.getCourseIntro();
		if (courseIntro != null) {
			response.put("data", courseIntro);
		} else {
			response.put("data", null); // 空資料
		}
		return ResponseEntity.ok(response); // 返回 200 OK，資料即使為空
	}

	@PostMapping("/savecoursesummary")
	public ResponseEntity<String> saveCourseSummary(@RequestBody CourseInformation courseInformation) {
		String courseSummary = courseInformation.getCourseSummary();
		postCoursesService.saveCourseSummary(courseSummary); // 呼叫 Service 處理邏輯
		return ResponseEntity.ok("課程名稱已成功暫存：" + courseSummary);
	}
//課程總覽清空
		@PostMapping("refreshCourseSummary")
		public ResponseEntity<String> refreshCourseSummary() {
			postCoursesService.refreshCourseSummary();
			return ResponseEntity.ok("課程總覽已成功清除：");
		}

//讀取課程總覽回網頁
	@PostMapping("/loadcoursesummary")
	public ResponseEntity<Map<String, Object>> loadCourseSummary() {
		Map<String, Object> response = new HashMap<>();
		String courseSummary = postCoursesService.getCourseSummary();
		if (courseSummary != null) {
			response.put("data", courseSummary);
		} else {
			response.put("data", null); // 空資料
		}
		return ResponseEntity.ok(response); // 返回 200 OK，資料即使為空
	}

	@PostMapping("/test22")
	public ResponseEntity<String> saveCourseImage(@RequestBody CourseInformation courseInformation) {
		String base64Image = courseInformation.getCourseImage(); // 前端傳來的 Base64 字符串

		// 移除前綴"data:image/png;base64,"
		String base64Data = base64Image.split(",")[1];

		// 將 Base64 字符串轉換為 byte[]
		byte[] imageBytes = Base64.getDecoder().decode(base64Data);

		// 呼叫 Service 儲存圖片
		postCoursesService.saveCourseImages(imageBytes);

		// 返回成功訊息
		String responseMessage = "課程圖片已成功暫存，圖片大小：" + (imageBytes != null ? imageBytes.length : 0) + " bytes";
		return ResponseEntity.ok(responseMessage);
	}
//課程總覽清空
			@PostMapping("refreshCourseImages")
			public ResponseEntity<String> refreshCourseImages() {
				postCoursesService.refreshCourseImages();
				return ResponseEntity.ok("課程總覽已成功清除：");
			}

//讀取課程封面回網頁
	@PostMapping("/test23")
	public ResponseEntity<List<String>> loadCourseImage() {
		List<byte[]> imageList = postCoursesService.getCourseImages();
		if (!imageList.isEmpty()) {
			// 將每個 byte[] 圖片數據轉換為 base64 字符串
			List<String> base64Images = imageList.stream()
					.map(imageBytes -> "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes))
					.collect(Collectors.toList());
			return ResponseEntity.ok(base64Images);
		} else {
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	@PostMapping("/save")
	public ResponseEntity<Map<String, String>> saveFormHtml(@RequestBody String rawHtml) {
		Map<String, String> response = new HashMap<>();
		if (rawHtml == null || rawHtml.isEmpty()) {
			response.put("status", "error");
			response.put("message", "HTML data is empty or missing");
			return ResponseEntity.badRequest().body(response);
		}
//	    System.out.println("Received HTML data: " + rawHtml);
		postCoursesService.saveFormData(rawHtml);
		// 保存 HTML 或其他處理邏輯
		response.put("status", "success");
		response.put("message", "HTML form saved successfully!");
		return ResponseEntity.ok(response);
	}
//清空postclass2的html
	@PostMapping("refreshFormData")
	public ResponseEntity<String> refreshFormData() {
		postCoursesService.refreshFormData();
		return ResponseEntity.ok("課程postclass2格式已成功清除：");
	}

//讀取postclass2的html架構回網頁
	@PostMapping("/loadForm")
	public ResponseEntity<String> loadFormHtml() {
		// courseService.saveCourseName("java");
		String list = postCoursesService.getFormData();
		System.out.println(list);
		if (list != null && !list.isEmpty()) {
			return ResponseEntity.ok(list); // 返回 HTML 內容
		} else {
			return ResponseEntity.ok("{}");
		}

	}

	@PostMapping("/savecoursetodatabase")
	public ResponseEntity<String> saveCourseToDatabase(HttpSession session) {
		try {

			postCoursesService.saveCourseToDatabase(session); // 傳入整體資料，集中處理邏輯

			return ResponseEntity.ok("課程已成功保存到資料庫！");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("儲存失敗：" + e.getMessage());
		}
	}

}