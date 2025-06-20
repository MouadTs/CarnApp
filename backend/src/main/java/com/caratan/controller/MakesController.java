package com.caratan.controller;

import com.caratan.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/makes")
@CrossOrigin(origins = "*")
public class MakesController {
    
    @Autowired
    private CarService carService;
    
    @GetMapping
    public ResponseEntity<List<String>> getAllMakes() {
        List<String> makes = carService.getPopularMakes();
        return ResponseEntity.ok(makes);
    }
    
    @GetMapping("/{makeName}/models")
    public ResponseEntity<List<String>> getModelsByMake(@PathVariable String makeName) {
        // For now, return some sample models
        List<String> models = List.of("Model 1", "Model 2", "Model 3");
        return ResponseEntity.ok(models);
    }
} 