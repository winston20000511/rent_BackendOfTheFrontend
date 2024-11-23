package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.HouseService;

import org.springframework.stereotype.Controller;

@RestController
@RequestMapping("/api/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @PostMapping("/add")
    public ResponseEntity<String> addHouse(@RequestParam Map<String, String> params) {
        try {
            houseService.addHouse(params);
            return ResponseEntity.ok("House added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding house: " + e.getMessage());
        }
    }
}
