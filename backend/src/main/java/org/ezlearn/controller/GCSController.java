package org.ezlearn.controller;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import org.ezlearn.service.GCSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/gcs")
public class GCSController {

    private final GCSService gcsService;

    public GCSController(GCSService gcsService) {
        this.gcsService = gcsService;
    }

    @PostMapping("/getSignedUrl")
    public ResponseEntity<Map<String, Object>> getSignedUrl(
        @RequestParam("course_names") List<String> courseNames,
        @RequestParam("course_videos") List<MultipartFile> courseVideos) {

        try {
            Map<String, Object> response = gcsService.processFiles(courseNames, courseVideos);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "生成簽名URL時發生錯誤: " + e.getMessage()));
        }
    }
   
}
