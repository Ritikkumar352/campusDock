package com.campusDock.campusdock.service.ServiceImpl;

import com.campusDock.campusdock.dto.CreateUserDto;
import com.campusDock.campusdock.dto.OtpResponse;
import com.campusDock.campusdock.dto.OtpResponseStatus;
import com.campusDock.campusdock.entity.College;
import com.campusDock.campusdock.entity.Enum.UserRole;
import com.campusDock.campusdock.entity.User;
import com.campusDock.campusdock.repository.CollegeRepo;
import com.campusDock.campusdock.repository.UserRepo;
import com.campusDock.campusdock.service.AuthService;
import com.campusDock.campusdock.service.EmailService;
import com.campusDock.campusdock.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService
{
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final EmailService emailService;
    private final CollegeRepo collegeRepo;
    private final UserRepo userRepo;
    private final JwtService jwtService;

    public AuthServiceImpl(EmailService emailService, CollegeRepo collegeRepo, UserRepo userRepo, JwtService jwtService) {
        this.emailService = emailService;
        this.collegeRepo = collegeRepo;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<OtpResponse> sendOTP(CreateUserDto request) {
        String email = request.getEmail();
        String otp = generateOtp();

        // Save OTP for this email temporarily  -- TODO - Use Redis here
        otpStorage.put(email, otp);

        emailService.sendOtpEmail(email, otp);


        return ResponseEntity.ok(
                OtpResponse.builder()
                        .message("OTP sent to " + email)
                        .build()
        );
    }




    @Override
    public ResponseEntity<OtpResponseStatus> verify(String email, String otp) {
        String storedOtp = otpStorage.get(email);

        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(email); // discard OTP

            // ✅ Check if user already exists
            Optional<User> existingUser = userRepo.findByEmail(email);
            User user;

            if (existingUser.isPresent())
            {
                user = existingUser.get();
            }
            else
            {
                // ➕ Proceed to create a new user
                String domain = email.substring(email.indexOf("@") + 1);
                College college = collegeRepo.findByDomain(domain)
                        .orElseThrow(() -> new RuntimeException("College not registered for domain: " + domain));

                String user_name = email.substring(0, email.indexOf("."));

                user = new User();
                user.setName(user_name);
                user.setPassword("otp-login"); // TODO: Handle securely later
                user.setEmail(email);
                user.setCollege(college);

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

                userRepo.save(user);
            }

            // ✅ Generate JWT
            String token = jwtService.generateToken(user);


            return ResponseEntity.ok(
                    OtpResponseStatus.builder()
                            .success(true)
                            .message("OTP verified successfully.")
                            .token(token)
                            .build()
            );
        }
        else
        {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(OtpResponseStatus.builder()
                            .success(false)
                            .message("Invalid or expired OTP")
                            .build()
                    );
        }
    }

    private String generateOtp() {
        int otp = new Random().nextInt(9000) + 1000; // 4-digit
        return String.valueOf(otp);
    }



    //  For random email testing
//    @Override
//    public ResponseEntity<OtpResponseStatus> verify(String email, String otp) {
//        String storedOtp = otpStorage.get(email);
//
//        if (storedOtp != null && storedOtp.equals(otp)) {
//            otpStorage.remove(email); // discard OTP
//
//            return ResponseEntity.ok(
//                    OtpResponseStatus.builder()
//                            .success(true)
//                            .message("OTP verified successfully")
//                            .build()
//            );
//        } else {
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(OtpResponseStatus.builder()
//                            .success(false)
//                            .message("Invalid or expired OTP")
//                            .build()
//                    );
//        }
//    }
}
