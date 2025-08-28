package com.gugus.batch.scheduler;

import com.gugus.batch.service.BrandSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : gtg
 * @fileName : BrandScheduler
 * @date : 2025-08-24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BrandScheduler {

    private final BrandSyncService brandSyncService;

    @Value("${sync.brand.enabled:true}")
    private boolean enabled;

    @Value("${sync.brand.page-size:200}")
    private int pageSize;

    @Scheduled(cron = "${sync.brand.cron}", zone = "${sync.common.zone:Asia/Seoul}")
    public void run() {
        if (!enabled) return;
        log.info("[BrandSyncScheduler] start");

        try {
            brandSyncService.syncAll(pageSize);
            log.info("[BrandSyncScheduler] completed");
        } catch (Exception e) {
            log.error("[BrandSyncScheduler] failed", e);
        }
    }
}
