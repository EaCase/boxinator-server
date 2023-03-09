package com.example.boxinator.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "settings")
public class SettingsController {

    @GetMapping("/countries")
    public void getCountries() {
    }

    @PostMapping("/countries")
    public void addCountry() {
        // ?
    }

    @PutMapping("/countries/{id}")
    public void updateCountry(@PathVariable int id) {
    }
}
