package com.gugus.batch.scheduler;

import com.gugus.batch.service.AppraisalPointSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : gtg
 * @fileName : AppraisalPointScheduler
 * @date : 2025-01-27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AppraisalPointScheduler {

    private final AppraisalPointSyncService appraisalPointSyncService;

    @Value("${sync.appraisal-point.enabled:true}")
    private boolean enabled;

    @Scheduled(cron = "${sync.appraisal-point.cron}", zone = "${sync.common.zone:Asia/Seoul}")
    public void run() {
        if (!enabled) return;
        log.info("[AppraisalPointScheduler] Appraisal point sync job started");
        try {
            appraisalPointSyncService.syncAppraisalPointByModelCode();
            log.info("[AppraisalPointScheduler] Appraisal point sync job completed");
        } catch (Exception e) {
            log.error("[AppraisalPointScheduler] Appraisal point sync job failed", e);
        }
    }
}
