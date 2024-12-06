package com.example.dummyjson.service;

import org.springframework.stereotype.Service;

@Service
public class HealthService {
    public static enum HealthStatus{OK, FAILURE, ABORTED};

    public HealthStatus isApiHealthy(){
        return HealthStatus.OK;
    }
}
