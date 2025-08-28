package com.gugus.batch.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : smk
 * @fileName : HealthCheckController
 * @date : 2025. 7. 30.
 */
@RestController
@Tag(name = "Health Check", description = "시스템 상태 확인 API")
public class HealthCheckController {
    
    @Operation(summary = "헬스 체크", description = "시스템의 상태를 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "시스템 정상"),
        @ApiResponse(responseCode = "500", description = "시스템 오류")
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
