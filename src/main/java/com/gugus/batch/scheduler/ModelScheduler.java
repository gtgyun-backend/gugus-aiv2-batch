package com.gugus.batch.scheduler;

import com.gugus.batch.service.BrandCategoryPairProvider;
import com.gugus.batch.service.ModelSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : gtg
 * @fileName : ModelScheduler
 * @date : 2025-08-23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ModelScheduler {

    private final ModelSyncService modelSyncService;
    private final BrandCategoryPairProvider pairProvider;

    @Value("${sync.model.enabled:true}")
    private boolean enabled;

    @Value("${sync.model.page-size:200}")
    private int pageSize;

    @Scheduled(cron = "${sync.model.cron}", zone = "${sync.common.zone:Asia/Seoul}")
    public void run() {
        if (!enabled) return;
        log.info("[ManagementModelSyncScheduler] start");

        try {
            // 모든 브랜드-카테고리 조합에 대해 모델 동기화 실행
            var pairs = pairProvider.allPairsForModels();
            log.info("[ManagementModelSyncScheduler] model pairs = {}", pairs.size());

            for (var pair : pairs) {
                try {
                    log.info("[ManagementModelSyncScheduler] model sync start {}-{}", pair.brandCode(), pair.categoryCode());
                    modelSyncService.syncPair(pair.brandCode(), pair.categoryCode(), pageSize);
                } catch (Exception e) {
                    log.error("[ManagementModelSyncScheduler] model sync failed {}-{}", pair.brandCode(), pair.categoryCode(), e);
                }
            }
            
            log.info("[ManagementModelSyncScheduler] completed");
        } catch (Exception e) {
            log.error("[ManagementModelSyncScheduler] failed", e);
        }
    }
}
