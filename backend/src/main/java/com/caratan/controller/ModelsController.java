package com.caratan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/models")
@CrossOrigin(origins = "*")
public class ModelsController {
    
    @GetMapping("/{modelName}/types")
    public ResponseEntity<List<String>> getTypesByModel(@PathVariable String modelName) {
        // For now, return some sample types
        List<String> types = List.of("Sedan", "SUV", "Hatchback", "Coupe");
        return ResponseEntity.ok(types);
    }
} 