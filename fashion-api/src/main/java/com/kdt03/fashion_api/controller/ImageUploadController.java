package com.kdt03.fashion_api.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/imageupload")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ImageUploadController {

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "파일 없음");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            String originalFilename = file.getOriginalFilename();
            String savedFilename = UUID.randomUUID() + "_" + originalFilename;

            String uploadDir = "C:/workspace_fashion/uploads/";
            File dest = new File(uploadDir + savedFilename);

            if (!dest.getParentFile().exists())
                dest.getParentFile().mkdirs();
            file.transferTo(dest);
            response.put("success", true);
            response.put("fileName", savedFilename);
            response.put("message", "업로드 성공");
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류");
            return ResponseEntity.internalServerError().body(response);
        }
    }

}
