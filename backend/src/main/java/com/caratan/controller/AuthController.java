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
            // Check if user already exists
            if (userRepository.existsByEmail(request.getEmail())) {
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
            
            return ResponseEntity.ok(AuthResponse.success("User registered successfully", token, userDto));
            
        } catch (Exception e) {
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
} 