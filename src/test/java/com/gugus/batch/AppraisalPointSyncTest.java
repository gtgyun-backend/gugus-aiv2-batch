package com.gugus.batch;

import com.gugus.batch.service.AppraisalPointSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * AppraisalPointScheduler를 테스트 클래스로 변환
 * @author : gtg
 * @date : 2025-01-27
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@RequiredArgsConstructor
public class AppraisalPointSyncTest {

    private final AppraisalPointSyncService appraisalPointSyncService;

    @Test
    public void syncAppraisalPoints() {
        log.info("[AppraisalPointSyncTest] Appraisal point sync job started");
        try {
            appraisalPointSyncService.syncAppraisalPointByModelCode();
            log.info("[AppraisalPointSyncTest] Appraisal point sync job completed");
        } catch (Exception e) {
            log.error("[AppraisalPointSyncTest] Appraisal point sync job failed", e);
            throw e;
        }
    }
}
