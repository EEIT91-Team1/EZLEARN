package org.ezlearn.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezlearn.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
	@Autowired
	private SearchRepository courseRepository;

	public List<Map<String, String>> search(String query, String page, String type, String price, String rate,
			String sort) {
		String[] queryArray =query.split(" ");
		String queryKey = "%";
		for(String key : queryArray) {
			queryKey += key + "%";
		}
		System.out.println(queryKey);
		Integer pages = (Integer.parseInt(page) - 1) * 10;
		if ("".equals(type)) {
			type = null;
		}
		if ("".equals(price)) {
			price = null;
		}
		if ("".equals(rate)) {
			rate = null;
		}
		List<Object[]> list;
		System.out.println(sort);
		switch (sort) {
		case "pt": {
			list = courseRepository.searchOrderByPriceT(queryKey, pages, type, price, rate);
			break;
		}
		case "pb": {
			list = courseRepository.searchOrderByPriceB(queryKey, pages, type, price, rate);
			break;
		}
		case "students": {
			list = courseRepository.searchOrderByStudents(queryKey, pages, type, price, rate);
			break;
		}
		case "rate": {
			list = courseRepository.searchOrderByRate(queryKey, pages, type, price, rate);
			break;
		}
		default: {
			list = courseRepository.searchOrderByCourseId(queryKey, pages, type, price, rate);
			break;
		}
		}

		List<Map<String, String>> courses = new ArrayList<Map<String, String>>();
		for (Object[] course : list) {
			Map<String, String> courseMap = new HashMap<String, String>();
			courseMap.put("courseName", (String) course[0]);
			courseMap.put("courseIntro", (String) course[1]);
			String imgBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString((byte[]) course[2]);
			courseMap.put("courseImg", imgBase64);
			courseMap.put("price", course[3] + "");
			courseMap.put("teacherName", (String) course[4]);
			courseMap.put("purchaseCount", course[5] + "");
			courseMap.put("courseRate", String.format("%.1f", course[6]));
			courseMap.put("courseId",course[7]+"");	
			courses.add(courseMap);
		}

		return courses;
	}

	public Map<String, Long> searchCount(String query) {
		String[] queryArray =query.split(" ");
		String queryKey = "%";
		for(String key : queryArray) {
			queryKey += key + "%";
		}
		List<Object[]> list = courseRepository.searchCount(queryKey);
		Map<String, Long> countMap = new HashMap<>();
		for (Object[] count : list) {
			countMap.put("courseCount", (Long) count[0]);
			countMap.put("languageCount", (Long) count[1]);
			countMap.put("programCount", (Long) count[2]);
			countMap.put("cookCount", (Long) count[3]);
			countMap.put("artCount", (Long) count[4]);
			countMap.put("sportCount", (Long) count[5]);
			countMap.put("financeCount", (Long) count[6]);
		}
		return countMap;
	}

}
