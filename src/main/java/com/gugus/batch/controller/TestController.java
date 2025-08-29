package com.gugus.batch.controller;

import com.gugus.batch.job.SyncJob;
import com.gugus.batch.service.AppraisalPointSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 테스트용 컨트롤러
 * 배치 작업을 수동으로 실행할 수 있는 엔드포인트 제공
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Tag(name = "Test Controller", description = "배치 작업 테스트용 API")
public class TestController {

    private final SyncJob syncJob;

    /**
     * 전체 동기화 작업을 수동으로 실행 (실제 API 호출)
     * @param pageSize 페이지 크기 (기본값: 200)
     * @return 실행 결과 메시지
     */
    @Operation(summary = "전체 동기화 실행", description = "실제 외부 API를 호출하여 브랜드, 카테고리, 모델 데이터를 동기화합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "동기화 성공"),
        @ApiResponse(responseCode = "500", description = "동기화 실패")
    })
    @PostMapping("/sync-all")
    public String runFullSync(@Parameter(description = "페이지 크기 (기본값: 200)") @RequestParam(defaultValue = "200") int pageSize) {
        log.info("[TestController] Manual sync started with pageSize={}", pageSize);
        
        try {
            syncJob.runAll(pageSize);
            return "Full sync completed successfully";
        } catch (Exception e) {
            log.error("[TestController] Manual sync failed", e);
            return "Full sync failed: " + e.getMessage();
        }
    }

    /**
     * 테스트용 전체 동기화 작업을 수동으로 실행 (JSON 파일 사용)
     * @param pageSize 페이지 크기 (기본값: 200)
     * @return 실행 결과 메시지
     */
    @Operation(summary = "테스트 데이터로 동기화 실행", description = "JSON 테스트 파일을 사용하여 브랜드, 카테고리, 모델 데이터를 동기화합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "동기화 성공"),
        @ApiResponse(responseCode = "500", description = "동기화 실패")
    })
    @PostMapping("/sync-all-test")
    public String runFullSyncWithTestData(@Parameter(description = "페이지 크기 (기본값: 200)") @RequestParam(defaultValue = "200") int pageSize) {
        log.info("[TestController] Manual sync with test data started with pageSize={}", pageSize);
        
        try {
            syncJob.runAllWithTestData(pageSize);
            return "Full sync with test data completed successfully";
        } catch (Exception e) {
            log.error("[TestController] Manual sync with test data failed", e);
            return "Full sync with test data failed: " + e.getMessage();
        }
    }
   
}
