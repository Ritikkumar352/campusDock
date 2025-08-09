package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.DetailedMenuItemDto;
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
    public ResponseEntity<Map<String, Object>> gitaddMenuItem(
            UUID canteenId,
            MenuItemRequestDto dto,
            List<MultipartFile> files
    ) {
        System.out.println("saving menu item--start");
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> uploadedMediaList = new ArrayList<>();

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

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        MediaFile media = mediaFileService.uploadMedia(file);
                        media.setMenuItems(savedMenuItem);
                        mediaFileService.save(media);

                        Map<String, String> mediaDetails = new HashMap<>();
                        mediaDetails.put("mediaId", media.getId().toString());
                        mediaDetails.put("url", media.getUrl());
                        uploadedMediaList.add(mediaDetails);

                    } catch (Exception e) {
                        System.err.println("Media upload failed: " + e.getMessage());
                    }
                }
            }
        }

        response.put("menuItemId", savedMenuItem.getId().toString());
        response.put("status", "Menu item created successfully");
        response.put("mediaFiles", uploadedMediaList);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    // 2. Get List of all Menu Item in a canteen
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

    // 3. Get Detail of a Menu Item
    public DetailedMenuItemDto getMenuItem(UUID id) {
        MenuItems menuItem = menuItemsRepo.findById(id).orElseThrow(() -> new RuntimeException("Menu item not found"));
        List<MediaFile> mediaFiles = menuItem.getMediaFile();
        List<String> urls = new ArrayList<>();
        for(MediaFile mediaFile : mediaFiles) {
            urls.add(mediaFile.getUrl());
        }

        return DetailedMenuItemDto.builder()
                .id(menuItem.getId())
                .foodName(menuItem.getFoodName())
                .description(menuItem.getDescription())
                .isAvailable(menuItem.isAvailable())
                .timeToCook(menuItem.getTimeToCook())
                .urls(urls)
                .build();
    }


}
