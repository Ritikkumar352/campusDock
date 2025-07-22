package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.CanteenDto;
import com.campusDock.campusdock.dto.CanteenListDto;
import com.campusDock.campusdock.dto.CanteenRequestDto;
import com.campusDock.campusdock.entity.Canteen;
import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.MediaFile;
import com.campusDock.campusdock.repository.CanteenRepo;
import com.campusDock.campusdock.repository.CollegeRepo;
import com.campusDock.campusdock.service.CanteenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class CanteenServiceImpl implements CanteenService {
    private final CanteenRepo canteenRepo;
    private final MediaFileServiceImpl mediaFileServiceImpl;
    private final CollegeRepo collegeRepo;

    public CanteenServiceImpl(CanteenRepo canteenRepo, MediaFileServiceImpl mediaFileServiceImpl, CollegeRepo collegeRepo) {
        this.canteenRepo = canteenRepo;
        this.mediaFileServiceImpl = mediaFileServiceImpl;
        this.collegeRepo = collegeRepo;
    }

    // 1. Register Canteen
    public ResponseEntity<Map<String, String>> registerCanteen(
            CanteenRequestDto canteenRequestDto,
            MultipartFile file
    ) {
        Map<String, String> response = new HashMap<>();
//        System.out.println("Received file: " + file.getOriginalFilename());

        // save canteen
        College college = collegeRepo.findById(canteenRequestDto.getCollege())
                .orElseThrow(() -> new RuntimeException("College not found"));

        Canteen canteen = Canteen.builder()
                .name(canteenRequestDto.getName())
                .description(canteenRequestDto.getDescription())
                .isOpen(canteenRequestDto.isOpen())
                .college(college) // Set actual College entity and receiving
                .createdAt(LocalDateTime.now().toString())
                .build();
        // save canteen
        Canteen savedCanteen = canteenRepo.save(canteen);

        if (file!=null) {
            try {
                MediaFile media = mediaFileServiceImpl.uploadMedia(file);
                media.setCanteen(savedCanteen);
                mediaFileServiceImpl.save(media);
                response.put("canteen_id", savedCanteen.getId().toString());
                response.put("media_id", media.getId().toString());
                response.put("status", "Canteen and media saved successfully");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("error", "Canteen saved but media uplload failedd: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
        response.put("canetten_id",savedCanteen.getId().toString());
        return ResponseEntity.ok(response);
    }

    // 2. get all canteen
    public List<CanteenListDto> getAllCanteens(UUID collegeId) {
        List<Canteen> canteens = canteenRepo.findByCollegeId(collegeId);

        List<CanteenListDto> response = new ArrayList<>();
        for (Canteen canteen : canteens) {
            CanteenListDto dto = CanteenListDto.builder()
                    .id(canteen.getId())
                    .name(canteen.getName())
                    .isOpen(canteen.isOpen())
                    .build();
            response.add(dto);
        }
        return response;

    }


    // 2. Get Canteen Details by Id
    // TODO -> Generate view url(per or temp)
    public ResponseEntity<CanteenDto> getCanteenById(UUID canteen_id) {
        Canteen foundCanteen = canteenRepo.findById(canteen_id)
                .orElseThrow(() -> new RuntimeException("Canteen not found"));

        String mediaUrl = null;
        if (foundCanteen.getMediaFile() != null && !foundCanteen.getMediaFile().isEmpty()) {
            mediaUrl = foundCanteen.getMediaFile().get(0).getUrl();
        }

        CanteenDto canteenDto = CanteenDto.builder()
                .id(foundCanteen.getId())
                .name(foundCanteen.getName())
                .description(foundCanteen.getDescription())
                .isOpen(foundCanteen.isOpen())
                .createdAt(foundCanteen.getCreatedAt())
                .collegeId(foundCanteen.getCollege().getId())
                .mediaUrl(mediaUrl)
                .build();

        return ResponseEntity.ok(canteenDto);
    }

//    public ResponseEntity<CanteenDto> getAll() {
//        List<Canteen> canteens = canteenRepo.findAll();
//
//        List<CanteenDto> response = new ArrayList<>();
//        for (Canteen canteen : canteens) {
//            CanteenDto dto = CanteenDto.builder()
//                    .id(canteen.getId())
//                    .name(canteen.getName())
//                    .description((canteen.getDescription()))
//                    .isOpen(canteen.isOpen())
//                    .createdAt(canteen.getCreatedAt())
//                    .collegeId(getCollegeId(canteen.getCollege()))
//                    .mediaUrl(getMediaUrl(canteen.getMediaFile()))
//                    .build();
//            response.add(dto);
//        }
//        return response;
//    }
//
//    private String getMediaUrl(List<MediaFile> mediaFile) {
//    }
//
//    private UUID getCollegeId(College college) {
//        return null;
//
//    }
}
