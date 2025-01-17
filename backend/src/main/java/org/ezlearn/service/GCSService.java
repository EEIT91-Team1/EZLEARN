package org.ezlearn.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.ezlearn.model.Courses;
import org.ezlearn.model.Lessons;
import org.ezlearn.repository.LessonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;



@Service
public class GCSService {
	
	
	private final Storage storage;
    private final String bucketName = "twhongexample";
    @Autowired
    private LessonsRepository lessonsRepository;

    public GCSService() throws IOException {
    	this.lessonsRepository=lessonsRepository;
        storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream("src/main/resources/gcs-service-key.json")))
                .setProjectId("testproject-446611")
                .build()
                .getService();
    }

    public Storage getStorage() {
        return storage;
    }

    public String getBucketName() {
        return bucketName;
    }
    
    public URL generateSignedUrl(String fileName) throws IOException {
        return storage.signUrl(
                BlobInfo.newBuilder(bucketName, fileName).build(),
                15, // Expiration time
                TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature(),
                Storage.SignUrlOption.httpMethod(HttpMethod.PUT)
        );
    }
    public Map<String, Object> processFiles(List<String> courseNames, List<MultipartFile> courseVideos) throws IOException {
        if (courseNames.size() != courseVideos.size()) {
            throw new IllegalArgumentException("課程名稱與影片數量不匹配");
        }

        Map<String, Object> response = new HashMap<>();
        for (int i = 0; i < courseNames.size(); i++) {
            String courseName = courseNames.get(i);
            MultipartFile courseVideo = courseVideos.get(i);

            String fileName = courseName + "_" + courseVideo.getOriginalFilename();
            URL signedUrl = generateSignedUrl(fileName);

            
            
            String publicUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
            saveLessonToDatabase(courseName, publicUrl);
            
            
            response.put("lesson" + (i + 1), Map.of(
                "lessonName", courseName,
                "fileName", fileName,
                "signedUrl", signedUrl.toString(),
                "publicUrl", publicUrl
            ));
        }
        return response;
    }
    public void saveLessonToDatabase(String lessonName, String videoUrl) {
        Lessons lesson = new Lessons();

        // 假設你有 Courses 的 repository，可以使用正確的資料填入
        Courses course = new Courses();
        

        lesson.setLessonName(lessonName);
        lesson.setVideoUrl(videoUrl);
        

        lessonsRepository.save(lesson); // 儲存進資料庫
    }
    
}
