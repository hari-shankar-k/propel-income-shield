package com.guide_wire.demo.controller;

import com.guide_wire.demo.security.JwtUtil;
import com.guide_wire.demo.service.DailyEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluate")
@RequiredArgsConstructor
public class EvaluationController {

    private final DailyEvaluationService dailyEvaluationService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public String evaluateUser(@RequestHeader("Authorization") String token) {

        String email = jwtUtil.extractEmail(token.substring(7));

        // 🔥 FIXED METHOD CALL
        dailyEvaluationService.evaluateSingleUser(email);

        return "Evaluation completed successfully";
    }
}