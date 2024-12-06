package com.example.dummyjson.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.dummyjson.service.HealthService;
import com.example.dummyjson.service.HealthService.HealthStatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("api/health")
public class HealthController {
    private final HealthService healthService;

    public HealthController(HealthService healthService){
        this.healthService = healthService;
    }

    @GetMapping
    public ResponseEntity<Map<String,Object>> getHealthStatus() {
        HttpStatus httpStatus = healthService.isApiHealthy() == HealthStatus.OK ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        Map<String, Object> mMap = new HashMap<>();
        mMap.put("health","Healthy");
        return ResponseEntity.status(httpStatus).body(mMap);
    }
    
    
}
