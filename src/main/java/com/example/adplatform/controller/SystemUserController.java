package com.example.adplatform.controller;

import com.example.adplatform.common.ApiResponse;
import com.example.adplatform.dto.SystemUserCreateRequest;
import com.example.adplatform.service.SystemUserService;
import com.example.adplatform.vo.SystemUserVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SystemUserController {

    private final SystemUserService systemUserService;

    @PostMapping
    public ApiResponse<SystemUserVO> create(@Valid @RequestBody SystemUserCreateRequest request) {
        return ApiResponse.success(systemUserService.create(request));
    }

    @GetMapping
    public ApiResponse<List<SystemUserVO>> list() {
        return ApiResponse.success(systemUserService.list());
    }

    @GetMapping("/{id}")
    public ApiResponse<SystemUserVO> detail(@PathVariable Long id) {
        return ApiResponse.success(systemUserService.getById(id));
    }
}
