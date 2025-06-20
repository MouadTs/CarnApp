package com.caratan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
@CrossOrigin(origins = "*")
public class ColorsController {
    
    @GetMapping
    public ResponseEntity<List<String>> getAllColors() {
        List<String> colors = List.of("White", "Black", "Red", "Blue", "Silver", "Gray", "Green", "Yellow", "Orange", "Purple");
        return ResponseEntity.ok(colors);
    }
} 