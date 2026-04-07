package com.guide_wire.demo.controller;

import com.guide_wire.demo.dto.BankRequest;
import com.guide_wire.demo.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @PostMapping("/add")
    public String addBank(@RequestBody BankRequest request) {
        return bankService.addBankDetails(request);
    }
}