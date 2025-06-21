package com.caratan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("usedCar_id")
    private Long id;
    
    @NotBlank
    @Column(name = "make")
    @JsonProperty("make_name")
    private String make;
    
    @NotBlank
    @Column(name = "model")
    @JsonProperty("model_name")
    private String model;
    
    @NotBlank
    @Column(name = "type")
    @JsonProperty("model_type")
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
    @JsonProperty("model_transmission")
    private String transmission;
    
    @NotBlank
    @Column(name = "fuel_type")
    @JsonProperty("model_fuel")
    private String fuelType;
    
    @Column(name = "engine_capacity")
    @JsonProperty("model_cc")
    private String engineCapacity = "1500"; // Default engine capacity
    
    @Column(name = "cylinder")
    @JsonProperty("model_cylinder")
    private String cylinder = "4"; // Default cylinder count
    
    @Column(name = "seats")
    @JsonProperty("model_seat")
    private String seats = "5"; // Default seat count
    
    @NotNull
    @Positive
    @Column(name = "price")
    private BigDecimal price;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "photos")
    @JsonProperty("car_mainPhoto")
    private String photos; // JSON array of photo URLs
    
    @Column(name = "views")
    private Integer views = 0;
    
    @Column(name = "location")
    private String location = "Jakarta"; // Default location
    
    @Transient
    @JsonProperty("make_logo")
    private String makeLogo = "/images/default_logo.png"; // Default logo
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @JsonIgnore
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
    
    public String getEngineCapacity() {
        return engineCapacity;
    }
    
    public void setEngineCapacity(String engineCapacity) {
        this.engineCapacity = engineCapacity;
    }
    
    public String getCylinder() {
        return cylinder;
    }
    
    public void setCylinder(String cylinder) {
        this.cylinder = cylinder;
    }
    
    public String getSeats() {
        return seats;
    }
    
    public void setSeats(String seats) {
        this.seats = seats;
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
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getMakeLogo() {
        return makeLogo;
    }
    
    public void setMakeLogo(String makeLogo) {
        this.makeLogo = makeLogo;
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