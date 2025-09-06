package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.CreateUserDto;
import com.campusDock.campusdock.dto.UserListDto;
import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.Enum.UserRole;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.repository.CollegeRepo;
import com.campusDock.campusdock.repository.UserRepo;
import com.campusDock.campusdock.service.JwtService;
import com.campusDock.campusdock.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final CollegeRepo collegeRepo;

    private final AuthServiceImpl authServiceImpl;
    private final JwtService jwtService;

    public UserServiceImpl(JwtService jwtService, UserRepo userRepo, CollegeRepo collegeRepo, AuthServiceImpl authServiceImpl) {
        this.userRepo = userRepo;
        this.collegeRepo = collegeRepo;
        this.authServiceImpl = authServiceImpl;
        this.jwtService = jwtService;
    }

//    @Override
//    public List<User> getAllUsers() {
//        return userRepo.findAll();
//    }

    public List<UserListDto> getUserList() {
        List<User> users = userRepo.findAll();
        List<UserListDto> userListDtos = new ArrayList<>();

        for (User user : users) {
            try {
                if (user != null && user.getEmail() != null) { // simple validation
                    //UserListDto dto = new UserListDto(user.getId(), user.getName(), user.getEmail(),user.getRole());
                    UserListDto dto=new UserListDto(user.getAnonymousName(),user.getEmail(),user.getId(),user.getName(),user.getRole());
                    userListDtos.add(dto);
                }
            } catch (Exception e) {
                System.err.println("Failed to convert user: " + user.getId());
            }
        }

        return userListDtos;
    }


    @Override
    public UserListDto createUser(CreateUserDto createUserDto) {

        String email = createUserDto.getEmail();
        String domain = email.substring(email.indexOf("@") + 1);
        College college = collegeRepo.findByDomain(domain)
                .orElseThrow(() -> new RuntimeException("College not registered for domain: " + domain));

        String user_name = email.substring(0, email.indexOf("."));

        User user = new User();
        user.setName(user_name);
        user.setPassword("otp-login"); // TODO: Handle securely later
        user.setEmail(email);
        user.setCollege(college);
        user.setAnonymousName(generateAnonymousName());

        // Determine role based on second part of email
        try {
            String afterFirstDot = email.substring(email.indexOf('.') + 1, email.indexOf('@'));
            if (afterFirstDot.matches(".*\\d.*")) {
                user.setRole(UserRole.STUDENT);
            } else {
                user.setRole(UserRole.FACULTY);
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid email format: " + email);
        }

        User saveduser = userRepo.save(user);


        // TODO -> Delete this
        System.out.println(jwtService.generateToken(saveduser));


        return UserListDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .anonymousName(user.getAnonymousName())
                .build();



    }

    @Override
    public User getUserById(UUID id) {
        return userRepo.findById(id).orElseThrow(()->new RuntimeException("User not found with user id:"+id));
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
