package com.campusDock.campusdock.service;

import com.campusDock.campusdock.entity.MediaFiles;
import com.campusDock.campusdock.repository.MediaFileRepo;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
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

    public ResponseEntity<Map<String, String>> uploadMedia(
            MultipartFile file
    ) {
        Map<String, String> response = new HashMap<>();
        Blob blob = null;
        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String contentType = file.getContentType();
        if(file.isEmpty()) response.put("status", "files cannot be empty");
        try {
            BlobId blobId = BlobId.of(bucketName, uniqueName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();
            blob = storage.create(blobInfo, file.getBytes());

            response.put("messaage", "File uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("response", "error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        boolean saveResponse=saveToDB(file);
        if(saveResponse) {
            response.put("status", "Saved to database");
        }else{
            response.put("status", "File uploaded but,MetaData is not save to database");
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    private boolean saveToDB(
        MultipartFile file
    ) {
        MediaFiles mediaFile= MediaFiles.builder()
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .type(file.getContentType())
                .build();
        MediaFiles res=mediaFileRepo.save(mediaFile);
        return !(res.getId() == null);
    }
}
