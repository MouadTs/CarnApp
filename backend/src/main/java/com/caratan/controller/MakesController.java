package com.caratan.controller;

import com.caratan.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/makes")
@CrossOrigin(origins = "*")
public class MakesController {
    
    @Autowired
    private CarService carService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMakes() {
        List<String> makes = carService.getPopularMakes();
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", false);
        response.put("carmake", makes);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> getAllMakesPost() {
        List<String> makes = carService.getPopularMakes();
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", false);
        response.put("carmake", makes);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{makeName}/models")
    public ResponseEntity<List<String>> getModelsByMake(@PathVariable String makeName) {
        // For now, return some sample models
        List<String> models = List.of("Model 1", "Model 2", "Model 3");
        return ResponseEntity.ok(models);
    }
} 