package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.entity.MediaFile;
import com.campusDock.campusdock.repository.MediaFileRepo;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class MediaFileServiceImpl {

    private final Storage storage;
    private final MediaFileRepo mediaFileRepo;
    @Value("${gcp.bucket.name}")
    private String bucketName;

    public MediaFileServiceImpl(Storage storage, MediaFileRepo mediaFileRepo) {
        this.storage = storage;
        this.mediaFileRepo = mediaFileRepo;
    }


    // upload and save
    public MediaFile uploadMedia(MultipartFile file) {
        if (file.isEmpty()) throw new IllegalArgumentException("File is empty");

        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String contentType = file.getContentType();

        try {
            // Upload to GCS
            BlobId blobId = BlobId.of(bucketName, uniqueName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();
            Blob blob = storage.create(blobInfo, file.getBytes());

            // Save to DB
            MediaFile mediaFile = MediaFile.builder()
                    .fileName(uniqueName)
                    .type(contentType)
                    .size(file.getSize())
                    .url("https://storage.googleapis.com/" + bucketName + "/" + uniqueName)
                    .build();
//            return mediaFileRepo.save(mediaFile);
             return save(mediaFile);
        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }

    private boolean saveToDB(MultipartFile file) {
        MediaFile mediaFile = MediaFile.builder()
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .type(file.getContentType())
                .build();
        MediaFile res = mediaFileRepo.save(mediaFile);
        return !(res.getId() == null);
    }

    public MediaFile save(MediaFile mediaFile) {
        return mediaFileRepo.save(mediaFile);
    }
}
