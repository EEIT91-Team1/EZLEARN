package org.ezlearn.model;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class CourseInformation {

    private String coursename;
    private String courseClass;
    private Integer coursePrice;
    private String courseIntro;
    private String courseSummary;
    private String courseImage;
    private Map<String, String> data;
    private Map<String, String> lesson;
    private List<String> lessonNamesList;
    
    
	
	public String getCourseSummary() {
		return courseSummary;
	}
	public void setCourseSummary(String courseSummary) {
		this.courseSummary = courseSummary;
	}
	public List<String> getLessonNamesList() {
		return lessonNamesList;
	}
	public void setLessonNamesList(List<String> lessonNamesList) {
		this.lessonNamesList = lessonNamesList;
	}
	public Map<String, String> getLesson() {
		return lesson;
	}
	public void setLesson(Map<String, String> lesson) {
		this.lesson = lesson;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public String getCourseClass() {
		return courseClass;
	}
	public void setCourseClass(String courseClass) {
		this.courseClass = courseClass;
	}
	
	
	public Integer getCoursePrice() {
		return coursePrice;
	}
	public void setCoursePrice(Integer coursePrice) {
		this.coursePrice = coursePrice;
	}
	public String getCourseIntro() {
		return courseIntro;
	}
	public void setCourseIntro(String courseIntro) {
		this.courseIntro = courseIntro;
	}
	public String getCourseImage() {
		return courseImage;
	}
	public void setCourseImage(String courseImage) {
		this.courseImage = courseImage;
	}

	
    
}