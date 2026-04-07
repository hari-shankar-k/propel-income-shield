package com.guide_wire.demo.controller;

import com.guide_wire.demo.dto.CompanyRequest;
import com.guide_wire.demo.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/join")
    public String joinCompany(@RequestBody CompanyRequest request) {
        return companyService.joinCompany(request);
    }
}