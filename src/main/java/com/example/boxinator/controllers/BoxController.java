package com.example.boxinator.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "box")
public class BoxController {

    @GetMapping("/tiers")
    public void getBoxTiers() {
    }
}
