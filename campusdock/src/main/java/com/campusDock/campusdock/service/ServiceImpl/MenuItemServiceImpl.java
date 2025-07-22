package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.MenuItemDto;
import com.campusDock.campusdock.dto.MenuItemRequestDto;
import com.campusDock.campusdock.entity.Canteen;
import com.campusDock.campusdock.entity.MediaFile;
import com.campusDock.campusdock.entity.MenuItems;
import com.campusDock.campusdock.repository.CanteenRepo;
import com.campusDock.campusdock.repository.MenuItemsRepo;
import com.campusDock.campusdock.service.MenuItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MenuItemServiceImpl implements MenuItemsService {

    private final MenuItemsRepo menuItemsRepo;
    private final CanteenRepo canteenRepo;
    private final MediaFileServiceImpl mediaFileService;

    public MenuItemServiceImpl(MenuItemsRepo menuItemsRepo, CanteenRepo canteenRepo, MediaFileServiceImpl mediaFileService) {
        this.menuItemsRepo = menuItemsRepo;
        this.canteenRepo = canteenRepo;
        this.mediaFileService = mediaFileService;
    }


    // 1. Add menuItem
    @Override
    public ResponseEntity<Map<String, String>> addMenuItem(
            UUID canteenId,
            MenuItemRequestDto dto,
            List<MultipartFile> files
    ) {
        System.out.println("saving menu item--start");
        Map<String, String> response = new HashMap<>();
        Canteen canteen = canteenRepo.findById(canteenId)
                .orElseThrow(() -> new RuntimeException("Canteen not found"));

        MenuItems menuItems = MenuItems.builder()
                .foodName(dto.getFoodName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .isAvailable(dto.isAvailable())
                .timeToCook(dto.getTimeToCook())
                .canteen(canteen)
                .build();

        MenuItems savedMenuItem = menuItemsRepo.save(menuItems);

        if (files != null ) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        MediaFile media = mediaFileService.uploadMedia(file);
                        media.setMenuItems(savedMenuItem);
                        mediaFileService.save(media);

                        response.put("mediaId", media.getId().toString());  // only first media id return
                        response.put("url", media.getUrl());
                    } catch (Exception e) {
                        response.put("error", "Menu Item saved, media upload failed for one or more files");
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }

        response.put("menuItem_id", savedMenuItem.getId().toString());
        response.put("status", "Menu item created successfully  without media upload");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 2. Get a Menu Item
    public ResponseEntity<MenuItems> getMenuItem(UUID id) {
        MenuItems menuItem = menuItemsRepo.findById(id).orElseThrow(() -> new RuntimeException("Menu item not found"));
        return ResponseEntity.ok(menuItem);
    }

    public List<MenuItemDto> getItemsByCanteenId(UUID canteenId) {
        List<MenuItems> menuItems = menuItemsRepo.findByCanteen_Id(canteenId);
        List<MenuItemDto> items = new ArrayList<>();

        for (MenuItems item : menuItems) {
            String url = null;
            List<MediaFile> mediaFiles = item.getMediaFile();
            if (mediaFiles != null && !mediaFiles.isEmpty()) {
                url = mediaFiles.get(0).getUrl();
            }

            MenuItemDto dto = MenuItemDto.builder()
                    .id(item.getId())
                    .name(item.getFoodName())
                    .price(item.getPrice())
//                    .description(item.getDescription())
                    .is_available(item.isAvailable())
                    .url(url)
                    .build();
            items.add(dto);
        }

        return items;
    }

}
