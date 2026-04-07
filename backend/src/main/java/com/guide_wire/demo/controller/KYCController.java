package com.guide_wire.demo.controller;

import com.guide_wire.demo.dto.KycRequest;
import com.guide_wire.demo.service.KYCService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kyc")
@RequiredArgsConstructor
public class KYCController {

    private final KYCService kycService;

    @PostMapping("/upload")
    public String uploadKyc(@RequestBody KycRequest request) {
        return kycService.uploadKyc(request);
    }
}