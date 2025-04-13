package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.entity.Canteen;
import com.campusDock.campusdock.entity.DTO.CanteenRequestDto;
import com.campusDock.campusdock.entity.MediaFiles;
import com.campusDock.campusdock.repository.CanteenRepo;
import com.campusDock.campusdock.service.CanteenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@Service
public class CanteenServiceImpl implements CanteenService {
    private final CanteenRepo canteenRepo;
    private final MediaFileService mediaFileService;

    public CanteenServiceImpl(CanteenRepo canteenRepo, MediaFileService mediaFileService) {
        this.canteenRepo = canteenRepo;
        this.mediaFileService = mediaFileService;
    }

    public ResponseEntity<Map<String, String>> registerCanteen(
            CanteenRequestDto canteenRequestDto,
            MultipartFile file
    ) {
        Map<String, String> response = new HashMap<>();

        // save canteen
        Canteen canteen = Canteen.builder()
                .name(canteenRequestDto.getName())
                .description(canteenRequestDto.getDescription())
                .is_open(canteenRequestDto.is_open())
                .college_id(canteenRequestDto.getCollege_id())
                .created_at(canteenRequestDto.getCreated_at())
                .build();
        Canteen savedCanteen = canteenRepo.save(canteen);

        try {
            MediaFiles media = mediaFileService.uploadMedia(file);
            media.setCanteen_media(savedCanteen);
            mediaFileService.save(media);
            response.put("canteen_id", savedCanteen.getId().toString());
            response.put("media_id", media.getId().toString());
            response.put("status", "Canteen and media saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Canteen saved but media uplload failedd: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
