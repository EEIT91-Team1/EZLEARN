package org.ezlearn.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezlearn.repository.IndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexService {
	
	@Autowired
	private IndexRepository indexRepository;

	public List<Map<String, String>> searchCourse() {

		List<Object[]> list = indexRepository.searchCourse();
		List<Map<String, String>> courses = new ArrayList<Map<String, String>>();
		for (Object[] course : list) {
			Map<String, String> courseMap = new HashMap<>();
			courseMap.put("courseId", course[0] + "");
			courseMap.put("courseName", (String) course[1]);
			String imgBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString((byte[]) course[2]);
			courseMap.put("courseImg", imgBase64);
			courseMap.put("price", course[3] + "");
			courseMap.put("teacherName", (String) course[4]);
			courseMap.put("students", course[5] + "");
			courseMap.put("courseRate", String.format("%.1f", course[6]));
			courses.add(courseMap);
		}
		return courses;
	}

	public List<Map<String, String>> review() {
		List<Object[]> list = indexRepository.review();
		List<Map<String, String>> courses = new ArrayList<Map<String, String>>();
		for (Object[] course : list) {
			Map<String, String> courseMap = new HashMap<>();
			courseMap.put("courseId", course[0] + "");
			String imgBase64 = "noImg";
			if((byte[]) course[1]!=null &&!(Base64.getEncoder().encodeToString((byte[]) course[1])).isEmpty()) {
				imgBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString((byte[]) course[1]);				
				}
			courseMap.put("avatar", imgBase64);
			courseMap.put("userName", (String) course[2]);
			courseMap.put("review", (String) course[3]);
			courseMap.put("rate", course[4] + "");
			courseMap.put("courseName", (String) course[5]);
			String strTime = course[6].toString();

			courseMap.put("time", (String) strTime.subSequence(0, strTime.length() - 2));
			courses.add(courseMap);
		}
		return courses;
	}

}
