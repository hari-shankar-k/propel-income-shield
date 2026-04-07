package com.guide_wire.demo.service;

import com.guide_wire.demo.dto.UserResponse;
import com.guide_wire.demo.entity.User;
import com.guide_wire.demo.repository.UserRepository;
import com.guide_wire.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ REGISTER
    // ✅ REGISTER
    public UserResponse register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        // 🔥 ADD THIS LINE (VERY IMPORTANT)
        user.setIsVerified(false);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getWorkType(),
                savedUser.getCreatedAt()
        );
    }

    // ✅ LOGIN
    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(email);   // ✅ FIXED
        } else {
            throw new RuntimeException("Invalid password");
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}