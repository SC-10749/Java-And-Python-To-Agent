package com.example.adplatform.controller;

import com.example.adplatform.common.ApiResponse;
import com.example.adplatform.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @GetMapping
    public ApiResponse<String> health() {
        return ApiResponse.success(healthService.check());
    }
}
