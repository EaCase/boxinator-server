package com.example.boxinator.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "account")
public class AccountController {

    @GetMapping("/{id}")
    public void getAccount(@PathVariable Long id) {
    }

    @PutMapping("/{id}")
    public void updateAccount(@PathVariable Long id) {
    }

    @PostMapping("/")
    public void createAccount() {
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        // TODO Admin only
    }
}
