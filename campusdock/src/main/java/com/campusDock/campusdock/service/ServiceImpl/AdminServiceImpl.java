package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.CanteenOwnerRegisterDto;
import com.campusDock.campusdock.entity.Canteen;
import com.campusDock.campusdock.entity.Enum.UserRole;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.repository.CanteenRepo;
import com.campusDock.campusdock.repository.UserRepo;
import com.campusDock.campusdock.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {
    private final CanteenRepo canteenRepo;

    public AdminServiceImpl(UserRepo userRepo, CanteenRepo canteenRepo) {
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
                .anonymousName(generateAnonymousName())
                .build();

        canteen.setOwner(owner);

        Canteen savedCanteen = canteenRepo.save(canteen);

        return savedCanteen.getOwner().getId().toString();
    }


    // 2. Get Canteen Owner
    @Override
    public Map<String, String> getCanteenOwner(UUID canteenId) {
        try {
            Canteen canteen = canteenRepo.findById(canteenId)
                    .orElse(null);

            if (canteen == null || canteen.getOwner() == null) {
                return null;
            }

            User owner = canteen.getOwner();
            Map<String, String> ownerDetails = new HashMap<>();
            ownerDetails.put("name", owner.getName());
            ownerDetails.put("email", owner.getEmail());

            return ownerDetails;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateAnonymousName() {
        String[] adjectives = {"AuraFarming", "BudgetBaba", "Desi", "PhalFruits", "PeecheKa",
                "Bollywood", "Chillam", "SwagWala", "TotallyPakka", "Mast",
                "AndazApna", "Thalaivar", "KahaniMein", "Jugaadu", "Sanskari",
                "Skibidi", "Delulu", "Brainrot", "Chill", "Dank",
                "Sigma", "NPC", "Based", "Goated", "Epic","Jugaadu", "SwagWala", "Mast", "Sanskari", "Bindaas", "Dhaasu",
                "Fadu", "Khatarnak", "Awaara", "Tirchi", "Ghumantu", "Baadshah",
                "Desi", "Chill", "Delulu", "Based", "Goated", "Epic",
                "Turbo", "Quantum", "Cosmic", "Shadow", "Mythic", "Divine",
                "Celestial", "Spicy", "Wobbly", "Zesty", "Whistling", "Blazing",
                "Digital", "Astra", "Vedic",
                "Phantom", "Eternal", "Silent", "Infernal", "Sacred"};


        String[] nouns = {
                "Shaktimaan", "Naagraj",
                 "Arjuna", "Karna", "Yodha",
                "Asura", "Rakshasa", "Garuda", "Trishul", "Chakra",
                "DramaKing", "BollywoodHero", "Mogambo", "Thakur", "Basanti","NPC", "System32", "404Error", "Memelord",
                "SigmaMale","Jalebi", "VadaPav", "Biskut", "Chai", "Coffee",
                "Robot", "Glitch", "Pixel", "ChillGuy", "Brainrotter", "DeluluKing", "AuraFarmer",
                "Goat", "NPCBot", "MemeRaja", "CryptoSadhu", "Techie",
                "BabaOP", "RickRoll", "BollywoodHero", "DramaKing", "Thalaivar",
                "SinghIsKing",  "NimbooWarrior", "Techie",
                "CoderYodha", "CryptoSadhu", "BollywoodDon", "MemeRaja",
                "DesiLegend", "404NotFound"};
        Random random = new Random();

        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
        int randomNumber = 100 + random.nextInt(900); // Generates a number between 100 and 999

        return adjective + noun + randomNumber;
    }
}
