package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.CanteenOwnerRegisterDto;
import com.campusDock.campusdock.entity.Canteen;
import com.campusDock.campusdock.entity.Enum.UserRole;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.repository.CanteenRepo;
import com.campusDock.campusdock.repository.UserRepo;
import com.campusDock.campusdock.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepo userRepo;
    private final CanteenRepo canteenRepo;
    public AdminServiceImpl(UserRepo userRepo,CanteenRepo canteenRepo) {

        this.userRepo = userRepo;
        this.canteenRepo = canteenRepo;
    }

    // 1. Canteen Owner Registration
    @Override
    public String registerOwner(CanteenOwnerRegisterDto dto) {
        Canteen canteen = canteenRepo.findById(dto.getCanteenId())
                .orElseThrow(() -> new IllegalArgumentException("Canteen not found with ID: " + dto.getCanteenId()));

        User owner = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword()) // TODO-> hash!!!
                .role(UserRole.CANTEEN_OWNER)
                .build();

        canteen.setOwner(owner);

        Canteen savedCanteen = canteenRepo.save(canteen);

        return savedCanteen.getOwner().getId().toString();
    }




}
