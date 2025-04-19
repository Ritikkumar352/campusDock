package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.entity.MediaFile;
import com.campusDock.campusdock.service.ServiceImpl.MediaFileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/media")
public class MediaFileController {

    private final MediaFileService mediaFileService;

    public MediaFileController(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }

    @PostMapping("/upload")
    public MediaFile uploadMedia(MultipartFile file) {
        return mediaFileService.uploadMedia(file);
    }

    // TODO -> create fetch photo and video controller


}
