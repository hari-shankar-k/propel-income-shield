package com.guide_wire.demo.controller;

import com.guide_wire.demo.dto.ClaimResponse;
import com.guide_wire.demo.service.ClaimService;
import com.guide_wire.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;
    private final JwtUtil jwtUtil;

    // 🧾 CREATE CLAIM
    @PostMapping
    public ClaimResponse createClaim(
            @RequestHeader("Authorization") String token,
            @RequestParam Long policyId,
            @RequestParam Double amount,
            @RequestParam String reason
    ) {
        String email = jwtUtil.extractEmail(token.substring(7));
        return claimService.createClaim(email, policyId, amount, reason);
    }

    // 📄 GET MY CLAIMS
    @GetMapping
    public List<ClaimResponse> getClaims(
            @RequestHeader("Authorization") String token
    ) {
        String email = jwtUtil.extractEmail(token.substring(7));
        return claimService.getUserClaims(email);
    }
    @GetMapping("/all")
    public List<ClaimResponse> getAllClaims() {
        return claimService.getAllClaims();
    }
}