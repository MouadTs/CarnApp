package com.caratan.dto;

public class AuthResponse {
    
    private boolean error;
    private String message;
    private String token;
    private UserDto user;
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }
    
    public AuthResponse(boolean error, String message, String token, UserDto user) {
        this.error = error;
        this.message = message;
        this.token = token;
        this.user = user;
    }
    
    // Getters and Setters
    public boolean isError() {
        return error;
    }
    
    public void setError(boolean error) {
        this.error = error;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public UserDto getUser() {
        return user;
    }
    
    public void setUser(UserDto user) {
        this.user = user;
    }
    
    // Static factory methods
    public static AuthResponse success(String message, String token, UserDto user) {
        return new AuthResponse(false, message, token, user);
    }
    
    public static AuthResponse error(String message) {
        return new AuthResponse(true, message);
    }
} 