package org.ezlearn.controller;

import java.util.List;
import java.util.Map;

import org.ezlearn.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private SearchService searchService;

	@GetMapping("/search")
	public ResponseEntity<List<Map<String, String>>> search(@RequestParam String query,
			@RequestParam(required = false, defaultValue = "1") String page,
			@RequestParam(required = false, defaultValue = "") String type,
			@RequestParam(required = false, defaultValue = "") String price,
			@RequestParam(required = false, defaultValue = "") String rate,
			@RequestParam(required = false, defaultValue = "") String sort) {
		List<Map<String, String>> results = searchService.search(query, page, type, price, rate, sort);
		return ResponseEntity.ok(results);
	}

	@GetMapping("/searchCount")
	public ResponseEntity<Map<String, Long>> searchCount(@RequestParam String query) {

		Map<String, Long> results = searchService.searchCount(query);
		return ResponseEntity.ok(results);
	}

}
