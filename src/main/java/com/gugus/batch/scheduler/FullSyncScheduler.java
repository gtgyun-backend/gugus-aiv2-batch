package com.gugus.batch.scheduler;

import com.gugus.batch.service.AppraisalPointSyncService;
import com.gugus.batch.service.BrandCategoryPairProvider;
import com.gugus.batch.service.BrandSyncService;
import com.gugus.batch.service.CategorySyncService;
import com.gugus.batch.service.ModelSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author : gtg
 * @fileName : FullSyncScheduler
 * @date : 2025-08-24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FullSyncScheduler {

    private final CategorySyncService categorySyncService;
    private final BrandSyncService brandSyncService;
    private final ModelSyncService modelSyncService;
    private final AppraisalPointSyncService appraisalPointSyncService;
    private final BrandCategoryPairProvider pairProvider;

    @Value("${sync.category.page-size:200}")
    private int categoryPageSize;

    @Value("${sync.brand.page-size:200}")
    private int brandPageSize;

    @Value("${sync.model.page-size:200}")
    private int modelPageSize;

    @Value("${sync.fullsync.enabled:true}")
    private boolean fullsyncEnabled;

    @Scheduled(cron = "0 30 19 * * *", zone = "Asia/Seoul") // 매일 새벽 2시 30분
    public void runFullSync() {
        log.info("[FullSyncScheduler] Full sync job started");

        try {
            // 1) 카테고리 동기화
            if(fullsyncEnabled){
            
                log.info("[FullSyncScheduler] Category sync started");
                categorySyncService.syncAll(categoryPageSize);
                log.info("[FullSyncScheduler] Category sync completed");
            
                log.info("[FullSyncScheduler] Brand sync started");
                brandSyncService.syncAll(brandPageSize);
                log.info("[FullSyncScheduler] Brand sync completed");

                // 3) 모델 동기화 (모든 브랜드-카테고리 조합)
                log.info("[FullSyncScheduler] Model sync started");
                var pairs = pairProvider.allPairsForModels();
                log.info("[FullSyncScheduler] Processing {} brand-category pairs", pairs.size());

                int successCount = 0;
                int failCount = 0;
                for (var pair : pairs) {
                    try {
                        modelSyncService.syncPair(pair.brandCode(), pair.categoryCode(), modelPageSize);
                        successCount++;
                    } catch (Exception e) {
                        log.error("[FullSyncScheduler] Model sync failed for {}-{}", pair.brandCode(), pair.categoryCode(), e);
                        failCount++;
                    }
                }
                log.info("[FullSyncScheduler] Model sync completed - Success: {}, Failed: {}", successCount, failCount);

                // 4) 감정포인트 동기화 (모든 모델)
                log.info("[FullSyncScheduler] Appraisal point sync started");
                try {
                    appraisalPointSyncService.syncAppraisalPointByModelCode();
                    log.info("[FullSyncScheduler] Appraisal point sync completed");
                } catch (Exception e) {
                    log.error("[FullSyncScheduler] Appraisal point sync failed", e);
                }

                log.info("[FullSyncScheduler] Full sync job completed successfully");
            }
        } catch (Exception e) {
            log.error("[FullSyncScheduler] Full sync job failed", e);
        }
    }

}
