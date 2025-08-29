package com.gugus.batch.scheduler;

import com.gugus.batch.service.CategorySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : gtg
 * @fileName : CategoryScheduler
 * @date : 2025-08-24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryScheduler {

    private final CategorySyncService categorySyncService;

    @Value("${sync.category.enabled:true}")
    private boolean enabled;

    @Value("${sync.category.page-size:200}")
    private int pageSize;

    @Scheduled(cron = "${sync.category.cron}", zone = "${sync.common.zone:Asia/Seoul}")
    public void run() {
        if (!enabled) return;
        log.info("[CategoryScheduler] Category sync job started");

        try {
            categorySyncService.syncAll(pageSize);
            log.info("[CategoryScheduler] Category sync job completed");
        } catch (Exception e) {
            log.error("[CategoryScheduler] Category sync job failed", e);
        }
    }
}
