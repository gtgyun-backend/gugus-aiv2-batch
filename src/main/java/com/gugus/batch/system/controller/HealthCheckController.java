package com.gugus.batch.system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : smk
 * @fileName : HealthCheckController
 * @date : 2025. 7. 30.
 */
@RestController
public class HealthCheckController {
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
