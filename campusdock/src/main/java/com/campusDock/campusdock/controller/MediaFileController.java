package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.service.MediaFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class MediaFileController {

    private final MediaFileService mediaFileService;
    public MediaFileController(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }

    @PostMapping("/uploadMedia")
    public ResponseEntity<Map<String,String>> uploadMedia(MultipartFile file) {
        return mediaFileService.uploadMedia(file);
    }


}
