package com.guide_wire.demo.controller;

import com.guide_wire.demo.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.guide_wire.demo.service.AuthService;
import com.guide_wire.demo.entity.User;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody User user) {
        return authService.register(user);
    }
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        String token = authService.login(user.getEmail(), user.getPassword());
        User loggedInUser = authService.getUserByEmail(user.getEmail());
        return Map.of("token", token, "userId", loggedInUser.getId().toString());
    }
}