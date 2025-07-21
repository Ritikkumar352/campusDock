package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.entity.MediaFile;
import com.campusDock.campusdock.repository.MediaFileRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MediaFileServiceImpl {

    private final S3Client s3Client;
    private final MediaFileRepo mediaFileRepo;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public MediaFileServiceImpl(S3Client s3Client, MediaFileRepo mediaFileRepo) {
        this.s3Client = s3Client;
        this.mediaFileRepo = mediaFileRepo;
    }

    // Upload and save to DB
    public MediaFile uploadMedia(MultipartFile file) {
        if (file.isEmpty()) throw new IllegalArgumentException("File is empty");

        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf('.'));
        String uniqueName = originalName.replace(ext, "") + "-" + UUID.randomUUID() + ext;
        String contentType = file.getContentType();

        try {
            // Upload to AWS S3 with public-read ACL
            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueName)
                    .contentType(contentType)
//                    .acl("public-read") //  make object publicly accessible
                    .build();

            s3Client.putObject(req, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Save metadata to DB
            MediaFile mediaFile = MediaFile.builder()
                    .fileName(uniqueName)
                    .type(contentType)
                    .size(file.getSize())
                    .url("https://" + bucketName + ".s3.amazonaws.com/" + uniqueName)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            return mediaFileRepo.save(mediaFile);

        } catch (Exception e) {
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
    }


    public MediaFile save(MediaFile mediaFile) {
        return mediaFileRepo.save(mediaFile);
    }
}
