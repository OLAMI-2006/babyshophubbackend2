package com.babyshophub.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.babyshophub.repository.UserRepository;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // You can add customer profile endpoints here later, e.g., getting/updating the logged-in user's profile
}