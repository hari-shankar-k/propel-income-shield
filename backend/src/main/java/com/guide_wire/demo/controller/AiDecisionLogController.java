package com.guide_wire.demo.controller;

import com.guide_wire.demo.entity.AiDecisionLog;
import com.guide_wire.demo.repository.AiDecisionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai/logs")
@RequiredArgsConstructor
public class AiDecisionLogController {

    private final AiDecisionLogRepository aiDecisionLogRepository;

    /**
     * 📊 GET ALL AI LOGS (PAGINATED)
     */
    @GetMapping
    public Page<AiDecisionLog> getAllLogs(Pageable pageable) {
        return aiDecisionLogRepository.findAll(pageable);
    }

    /**
     * 📊 GET LOG BY ID
     */
    @GetMapping("/{id}")
    public AiDecisionLog getLogById(@PathVariable Long id) {
        return aiDecisionLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found"));
    }
}