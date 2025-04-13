package com.campusDock.campusdock.controller;

import com.campusDock.campusdock.entity.MediaFiles;
import com.campusDock.campusdock.service.ServiceImpl.MediaFileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MediaFileController {

    private final MediaFileService mediaFileService;

    public MediaFileController(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }

    @PostMapping("/uploadMedia")
    public MediaFiles uploadMedia(MultipartFile file) {
        return mediaFileService.uploadMedia(file);
    }

    // TODO -> create fetch photo and video controller


}
