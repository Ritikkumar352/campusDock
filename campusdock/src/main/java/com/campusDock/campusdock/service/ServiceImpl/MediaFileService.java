package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.entity.MediaFiles;
import com.campusDock.campusdock.repository.MediaFileRepo;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class MediaFileService {

    private final Storage storage;
    private final MediaFileRepo mediaFileRepo;
    private final String bucketName = null;

    public MediaFileService(Storage storage, MediaFileRepo mediaFileRepo) {
        this.storage = storage;
        this.mediaFileRepo = mediaFileRepo;
    }


    // upload and save
    public MediaFiles uploadMedia(MultipartFile file) {
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
            MediaFiles mediaFile = MediaFiles.builder()
                    .fileName(uniqueName)
                    .type(contentType)
                    .size(file.getSize())
                    .URL("https://storage.googleapis.com/" + bucketName + "/" + uniqueName)
                    .build();

            return mediaFileRepo.save(mediaFile);
        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }

    private boolean saveToDB(MultipartFile file) {
        MediaFiles mediaFile = MediaFiles.builder()
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .type(file.getContentType())
                .build();
        MediaFiles res = mediaFileRepo.save(mediaFile);
        return !(res.getId() == null);
    }

    public MediaFiles save(MediaFiles mediaFile) {
        return mediaFileRepo.save(mediaFile);
    }
}
