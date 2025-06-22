package com.caratan.controller;

import com.caratan.dto.AuthResponse;
import com.caratan.dto.LoginRequest;
import com.caratan.dto.RegisterRequest;
import com.caratan.dto.UserDto;
import com.caratan.entity.User;
import com.caratan.repository.UserRepository;
import com.caratan.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            System.out.println("Registration request received: " + request.getFullName() + ", " + request.getEmail());
            
            // Check if user already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                System.out.println("User already exists with email: " + request.getEmail());
                return ResponseEntity.badRequest()
                        .body(AuthResponse.error("User with this email already exists"));
            }
            
            // Create new user
            User user = new User(
                    request.getFullName(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword())
            );
            
            User savedUser = userRepository.save(user);
            System.out.println("User saved successfully with ID: " + savedUser.getId());
            
            // Generate JWT token
            String token = jwtUtil.generateToken(savedUser.getEmail());
            
            // Create user DTO
            UserDto userDto = new UserDto(
                    savedUser.getId(),
                    savedUser.getFullName(),
                    savedUser.getEmail(),
                    savedUser.getPhone(),
                    savedUser.getProfilePic(),
                    savedUser.getIsAdmin()
            );
            
            System.out.println("Registration successful for: " + savedUser.getEmail());
            return ResponseEntity.ok(AuthResponse.success("User registered successfully", token, userDto));
            
        } catch (Exception e) {
            System.err.println("Registration failed with error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(AuthResponse.error("Registration failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Find user by email
            User user = userRepository.findByEmail(request.getEmail())
                    .orElse(null);
            
            if (user == null) {
                return ResponseEntity.badRequest()
                        .body(AuthResponse.error("Invalid email or password"));
            }
            
            // Check password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(AuthResponse.error("Invalid email or password"));
            }
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());
            
            // Create user DTO
            UserDto userDto = new UserDto(
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getProfilePic(),
                    user.getIsAdmin()
            );
            
            return ResponseEntity.ok(AuthResponse.success("Login successful", token, userDto));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(AuthResponse.error("Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<AuthResponse> updateUser(
            @RequestParam("user_id") String userId,
            @RequestParam("fullname") String fullname,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("profile_pic") String profilePic) {
        try {
            // Find user by ID
            User user = userRepository.findById(Long.parseLong(userId))
                    .orElse(null);
            
            if (user == null) {
                return ResponseEntity.badRequest()
                        .body(AuthResponse.error("User not found"));
            }
            
            // Handle profile picture upload
            String profilePicFilename = null;
            if (profilePic != null && !profilePic.isEmpty()) {
                try {
                    // Generate unique filename
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    profilePicFilename = "profile_" + userId + "_" + timestamp + ".jpg";
                    
                    // Save base64 image to file
                    saveBase64Image(profilePic, profilePicFilename);
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body(AuthResponse.error("Failed to save profile picture: " + e.getMessage()));
                }
            }
            
            // Update user fields
            user.setFullName(fullname);
            user.setPhone(phone);
            user.setEmail(email);
            if (profilePicFilename != null) {
                user.setProfilePic(profilePicFilename);
            }
            
            User updatedUser = userRepository.save(user);
            
            // Create user DTO
            UserDto userDto = new UserDto(
                    updatedUser.getId(),
                    updatedUser.getFullName(),
                    updatedUser.getEmail(),
                    updatedUser.getPhone(),
                    updatedUser.getProfilePic(),
                    updatedUser.getIsAdmin()
            );
            
            return ResponseEntity.ok(AuthResponse.success("Profile updated successfully", null, userDto));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(AuthResponse.error("Update failed: " + e.getMessage()));
        }
    }
    
    private void saveBase64Image(String base64Image, String filename) {
        try {
            // Remove data URL prefix if present
            String base64Data = base64Image;
            if (base64Image.contains(",")) {
                base64Data = base64Image.substring(base64Image.indexOf(",") + 1);
            }
            
            // Decode base64 to bytes
            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Data);
            
            // Create uploads directory if it doesn't exist
            String uploadDir = "uploads/";
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Save the file
            java.io.File file = new java.io.File(uploadDir + filename);
            java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
            fos.write(imageBytes);
            fos.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth controller is working!");
    }
} 