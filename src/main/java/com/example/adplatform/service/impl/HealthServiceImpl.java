package com.example.adplatform.service.impl;

import com.example.adplatform.service.HealthService;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImpl implements HealthService {

    @Override
    public String check() {
        return "ad-platform is running";
    }
}
