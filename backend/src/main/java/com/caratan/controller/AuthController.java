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
    public ResponseEntity<AuthResponse> updateUser(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("user_id");
            String fullname = request.get("fullname");
            String phone = request.get("phone");
            String email = request.get("email");
            String profilePic = request.get("profile_pic");
            
            // Find user by ID
            User user = userRepository.findById(Long.parseLong(userId))
                    .orElse(null);
            
            if (user == null) {
                return ResponseEntity.badRequest()
                        .body(AuthResponse.error("User not found"));
            }
            
            // Update user fields
            user.setFullName(fullname);
            user.setPhone(phone);
            user.setEmail(email);
            user.setProfilePic(profilePic);
            
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

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth controller is working!");
    }
} 