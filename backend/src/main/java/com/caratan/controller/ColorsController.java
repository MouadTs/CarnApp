package com.caratan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colors")
@CrossOrigin(origins = "*")
public class ColorsController {
    
    private static final Logger logger = LoggerFactory.getLogger(ColorsController.class);
    
    @GetMapping
    public ResponseEntity<List<String>> getAllColors() {
        logger.info("ColorsController.getAllColors() called");
        List<String> colors = List.of("White", "Black", "Red", "Blue", "Silver", "Gray", "Green", "Yellow", "Orange", "Purple");
        logger.info("Returning {} colors", colors.size());
        return ResponseEntity.ok(colors);
    }
} 