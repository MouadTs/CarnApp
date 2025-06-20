package com.caratan.controller;

import com.caratan.entity.Car;
import com.caratan.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
@CrossOrigin(origins = "*")
public class CarController {
    
    @Autowired
    private CarService carService;
    
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> car = carService.getCarById(id);
        if (car.isPresent()) {
            // Increment views using JDBC
            carService.incrementViews(id);
            return ResponseEntity.ok(car.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/makes")
    public ResponseEntity<List<String>> getAllMakes() {
        List<String> makes = carService.getPopularMakes();
        return ResponseEntity.ok(makes);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Car>> searchCars(@RequestParam String q) {
        List<Car> cars = carService.searchCars(q);
        return ResponseEntity.ok(cars);
    }
    
    @GetMapping("/price-range")
    public ResponseEntity<List<Car>> getCarsByPriceRange(
            @RequestParam double minPrice, 
            @RequestParam double maxPrice) {
        List<Car> cars = carService.getCarsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(cars);
    }
    
    @GetMapping("/year-range")
    public ResponseEntity<List<Car>> getCarsByYearRange(
            @RequestParam int minYear, 
            @RequestParam int maxYear) {
        List<Car> cars = carService.getCarsByYearRange(minYear, maxYear);
        return ResponseEntity.ok(cars);
    }
    
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car savedCar = carService.saveCar(car);
        return ResponseEntity.ok(savedCar);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        Optional<Car> existingCar = carService.getCarById(id);
        if (existingCar.isPresent()) {
            car.setId(id);
            Car updatedCar = carService.saveCar(car);
            return ResponseEntity.ok(updatedCar);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        Optional<Car> car = carService.getCarById(id);
        if (car.isPresent()) {
            carService.deleteCar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 