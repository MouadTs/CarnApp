package com.caratan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cars")
public class Car {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(name = "make")
    private String make;
    
    @NotBlank
    @Column(name = "model")
    private String model;
    
    @NotBlank
    @Column(name = "type")
    private String type;
    
    @NotNull
    @Column(name = "year")
    private Integer year;
    
    @NotBlank
    @Column(name = "color")
    private String color;
    
    @NotNull
    @Positive
    @Column(name = "mileage")
    private Integer mileage;
    
    @NotBlank
    @Column(name = "transmission")
    private String transmission;
    
    @NotBlank
    @Column(name = "fuel_type")
    private String fuelType;
    
    @NotNull
    @Positive
    @Column(name = "price")
    private BigDecimal price;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "photos")
    private String photos; // JSON array of photo URLs
    
    @Column(name = "views")
    private Integer views = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Car() {}
    
    public Car(String make, String model, String type, Integer year, String color, 
               Integer mileage, String transmission, String fuelType, BigDecimal price, 
               String description, User seller) {
        this.make = make;
        this.model = model;
        this.type = type;
        this.year = year;
        this.color = color;
        this.mileage = mileage;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.price = price;
        this.description = description;
        this.seller = seller;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMake() {
        return make;
    }
    
    public void setMake(String make) {
        this.make = make;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Integer getMileage() {
        return mileage;
    }
    
    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }
    
    public String getTransmission() {
        return transmission;
    }
    
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    
    public String getFuelType() {
        return fuelType;
    }
    
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPhotos() {
        return photos;
    }
    
    public void setPhotos(String photos) {
        this.photos = photos;
    }
    
    public Integer getViews() {
        return views;
    }
    
    public void setViews(Integer views) {
        this.views = views;
    }
    
    public User getSeller() {
        return seller;
    }
    
    public void setSeller(User seller) {
        this.seller = seller;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
} 