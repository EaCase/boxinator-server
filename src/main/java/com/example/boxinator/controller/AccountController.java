package com.example.boxinator.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "account")
public class AccountController {

    @GetMapping("/{id}")
    public void getAccount(@PathVariable int id) {
    }

    @PutMapping("/{id}")
    public void updateAccount(@PathVariable int id) {
    }

    @PostMapping("/")
    public void createAccount() {
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable int id) {
        // TODO Admin only
    }
}
