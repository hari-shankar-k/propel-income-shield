package com.guide_wire.demo.controller;

import com.guide_wire.demo.dto.PolicyRequest;
import com.guide_wire.demo.dto.PolicyResponse;
import com.guide_wire.demo.entity.Policy;
import com.guide_wire.demo.security.SecurityUtils;
import com.guide_wire.demo.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;


    @PostMapping("/select")
    public Policy createPolicy(@RequestBody Policy policy) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return policyService.createPolicyByEmail(email, policy);
    }


    @GetMapping
    public List<PolicyResponse> getPolicies() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return policyService.getPoliciesByUser(email);
    }
    @PostMapping("/purchase")
    public String purchasePolicy(@RequestBody PolicyRequest request) {

        String email = SecurityUtils.getCurrentUserEmail();

        policyService.purchasePolicy(email, request.getPlanName());

        return "Policy activated successfully";
    }@GetMapping("/active")
    public PolicyResponse getActivePolicy() {

        String email = SecurityUtils.getCurrentUserEmail();

        return policyService.getActivePolicy(email);
    }
}