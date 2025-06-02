package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.entity.Canteen;
import com.campusDock.campusdock.entity.DTO.MenuItemRequestDto;
import com.campusDock.campusdock.entity.MediaFile;
import com.campusDock.campusdock.entity.MenuItems;
import com.campusDock.campusdock.repository.CanteenRepo;
import com.campusDock.campusdock.repository.MenuItemsRepo;
import com.campusDock.campusdock.service.MenuItemsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MenuItemServiceImpl implements MenuItemsService {

    private final MenuItemsRepo menuItemsRepo;
    private final CanteenRepo canteenRepo;
    private final MediaFileServiceImpl mediaFileService;

    public MenuItemServiceImpl(MenuItemsRepo menuItemsRepo, CanteenRepo canteenRepo,MediaFileServiceImpl mediaFileService) {
        this.menuItemsRepo = menuItemsRepo;
        this.canteenRepo = canteenRepo;
        this.mediaFileService = mediaFileService;
    }

    // 1. Add menuItem
    public ResponseEntity<Map<String, String>> addMenuItem(
            MenuItemRequestDto dto,
            MultipartFile file
    ) {
        Map<String, String> response = new HashMap<>();
        Canteen canteen = canteenRepo.findById(dto.getCanteenId())
                .orElseThrow(() -> new RuntimeException("Canteen not found"));

        MenuItems menuItems = MenuItems.builder()
                .foodName(dto.getFoodName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .isAvailable(dto.isAvailable())
                .timeToCook(dto.getTimeToCook())
                .canteen(canteen)
                .build();
        // save menu Item
        MenuItems savedMenuItem = menuItemsRepo.save(menuItems);

        if(savedMenuItem.isAvailable()){
            response.put("menuItem_id", savedMenuItem.getId().toString());
            response.put("status", "Menu item created successfully");
            try{
                MediaFile media=mediaFileService.uploadMedia(file);
                media.setMenuItems(savedMenuItem);
                mediaFileService.save(media);

                response.put("mediaId",media.getId().toString());
                response.put("url",media.getUrl());
                return new ResponseEntity<>(response,HttpStatus.CREATED);
            }catch (Exception e){
                response.put("error","Menu Item saved without media upload ");
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }
        }




        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. Get a Menu Item
    public ResponseEntity<MenuItems> getMenuItem(UUID id) {
        MenuItems menuItem=menuItemsRepo.findById(id).orElseThrow(() -> new RuntimeException("Menu item not found"));
        return ResponseEntity.ok(menuItem);
    }
}
