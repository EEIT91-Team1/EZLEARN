package org.ezlearn.service;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.model.UserInfo;
import org.ezlearn.model.Users;
import org.ezlearn.repository.CoursesRepository;
import org.ezlearn.repository.LessonsRepository;
import org.ezlearn.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Service
public class PostCoursesService {
	@Autowired
    private CoursesRepository coursesRepository;
	
	@Autowired
    private UserInfoRepository userInfoRepository;
	@Autowired
	private LessonsRepository lessonsRepository;
	
	
    // 模擬資料儲存
    private  String courseName = new String();
    private  String courseClasse = new String();
    private  Integer coursePrice ;
    private  String courseIntro = new String();
    private  String courseSummary = new String();
    private List<byte[]> courseImages = new ArrayList<>();
    private String formData = new String();
    

    
    

   
    
    
    
    public void saveFormData(String data) {
        if (data != null && !data.isEmpty()) {
            formData = data;
//            System.out.println("已暫存表單數據：" + formData);
        } else {
            throw new IllegalArgumentException("表單數據不能為空");
        }
    }
    public void refreshFormData() {
    	formData="";
    	System.out.println("refreshFormData");
    	
    }


    public String getFormData() {
        return formData;
    }

    
   
    public void saveCourseName(String coursename) {
        courseName=coursename;
        System.out.println("已新增課程名稱：" + coursename); // 後端日誌
        
    }
    public void refreshCourseName() {
    	courseName="";
    	System.out.println("refreshName");
    	System.out.println(courseName);
    }


    public String getCourseName() {
        return courseName;
    }
    
    
    public void saveCourseClasse(String courseClass) {
    	courseClasse=courseClass;
    	System.out.println("已新增課程種類：" + courseClass); // 後端日誌
    	}
    
    public void refreshCourseClasse() {
    	courseClasse="";
    	System.out.println("refreshClasse");
    	System.out.println(courseClasse);
    }
    public String getCourseClasse() {
    	return courseClasse;
    }
    
    public void saveCoursePrice(Integer courseprice) {
        coursePrice=courseprice;
        System.out.println("已新增課程名稱：" + courseprice); // 後端日誌
        
    }
    public void refreshCoursePrice() {
    	coursePrice=null;
    	System.out.println("coursePrice");
    	 }

    public Integer getCoursePrice() {
        return coursePrice;
    }
    public void saveCourseIntro(String courseintro) {
    	courseIntro=courseintro;
    	System.out.println("已新增課程介紹：" + courseIntro); // 後端日誌
    	
    }
    public void refreshCourseIntro() {
    	courseIntro="";
    	System.out.println("courseIntro");
    	 }
    public String getCourseIntro() {
    	return courseIntro;
    }
    public void saveCourseSummary(String coursesummary) {
    	courseSummary=coursesummary;
    	System.out.println("已新增課程總覽：" + courseSummary); // 後端日誌
    	
    }
    public void refreshCourseSummary() {
    	courseSummary="";
    	System.out.println("courseSummary");
    	 }
    public String getCourseSummary() {
    	return courseSummary;
    }
    
    public void saveCourseImages(byte[] courseImage) {
    	courseImages.add(courseImage);
    	System.out.println("已新增課程介紹：" + courseImages); // 後端日誌
    	
    }
    public void refreshCourseImages() {
    	courseImages.clear();
    	System.out.println("courseImages");
    	 }
    public List<byte[]> getCourseImages() {
    	return courseImages;
    }
    
    @Transactional
    public void saveCourseToDatabase(HttpSession session) {
        // 創建 CourseModel 對象，並設置其屬性
        Courses course = new Courses();
        //teacher.setUserId((long)1);; // 假設 teacherId=1 已存在資料庫
        Users user = (Users)session.getAttribute("user");
        System.out.println(user);
		UserInfo userinfo = new UserInfo();
		userinfo.setUserId(user.getUserId());
       
       
       
//        UserInfo teacherid = new UserInfo(); 
//       Users user = (Users) session.getAttribute("user");
//       Users userSession = (Users) session.getAttribute("user");
//       System.out.println(userSession);
//       long a = (long)session.getAttribute("user");
//       teacherid.setUserId(a);
        course.setUserInfo(userinfo);;
        course.setCourseName(courseName);
        course.setCourseType(courseClasse);
        course.setPrice(coursePrice);
        course.setCourseIntro(courseIntro);
        course.setCourseSummary(courseSummary);
        
        if (!courseImages.isEmpty()) {
            course.setCourseImg(courseImages.get(0));; // 假設只取第一張圖片
        }

        // 通過 repository 保存
        Courses savedCourse = coursesRepository.save(course);
        System.out.println("課程已成功保存到數據庫！");

        // 取得保存後的課程ID
        Long courseId = savedCourse.getCourseId();
        System.out.println("新創建的課程ID: " + courseId);

        // 查找所有 `lesson` 中 `course_id` 為 `null` 的紀錄
        System.out.println("在這裡1");
        List<Lessons> lessonsToUpdate = lessonsRepository.findByCoursesIsNull();
        System.out.println("找到了 " + (lessonsToUpdate == null ? "null" : lessonsToUpdate.size()) + " 筆資料");
        System.out.println("在這裡");
        System.out.println("查詢結果：lessonsToUpdate size = " + lessonsToUpdate.size());
        if (!lessonsToUpdate.isEmpty()) {
            // 更新每一個 `lesson` 的 `course_id` 為新創建的課程的 `courseId`
            for (Lessons lesson : lessonsToUpdate) {
                lesson.setCourses(savedCourse);  // 設定新的課程 (這裡會自動包含課程 ID)
                lessonsRepository.save(lesson);   // 保存更新後的 lesson
                System.out.println("更新 Lesson ID " + lesson.getLessonId() + " 的 courseId 為 " + courseId);
            }
            System.out.println("所有 `course_id` 為 null 的 Lesson 記錄已更新為新的 course_id！");
        } else {
            System.out.println("沒有找到 `course_id` 為 null 的 Lesson 記錄。");
        }
    }
}