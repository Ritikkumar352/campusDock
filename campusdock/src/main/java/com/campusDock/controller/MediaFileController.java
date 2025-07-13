package com.campusDock.controller;

import com.campusDock.entity.MediaFile;
import com.campusDock.service.ServiceImpl.MediaFileServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/media")
public class MediaFileController {

    private final MediaFileServiceImpl mediaFileServiceImpl;

    public MediaFileController(MediaFileServiceImpl mediaFileServiceImpl) {
        this.mediaFileServiceImpl = mediaFileServiceImpl;
    }

    @PostMapping("/upload")
    public MediaFile uploadMedia(MultipartFile file) {
        return mediaFileServiceImpl.uploadMedia(file);
    }

    // TODO -> create fetch photo and video controller


}
