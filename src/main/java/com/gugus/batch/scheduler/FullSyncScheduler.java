package com.gugus.batch.scheduler;

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
    private final BrandCategoryPairProvider pairProvider;

    @Value("${sync.category.enabled:true}")
    private boolean categoryEnabled;

    @Value("${sync.brand.enabled:true}")
    private boolean brandEnabled;

    @Value("${sync.model.enabled:true}")
    private boolean modelEnabled;

    @Value("${sync.category.page-size:200}")
    private int categoryPageSize;

    @Value("${sync.brand.page-size:200}")
    private int brandPageSize;

    @Value("${sync.model.page-size:200}")
    private int modelPageSize;

    @Scheduled(cron = "0 30 2 * * *", zone = "Asia/Seoul") // 매일 새벽 2시 30분
    public void runFullSync() {
        log.info("[FullSyncScheduler] start");

        try {
            // 1) 카테고리 동기화
            if (categoryEnabled) {
                log.info("[FullSyncScheduler] category sync start");
                categorySyncService.syncAll(categoryPageSize);
                log.info("[FullSyncScheduler] category sync completed");
            }

            // 2) 브랜드 동기화
            if (brandEnabled) {
                log.info("[FullSyncScheduler] brand sync start");
                brandSyncService.syncAll(brandPageSize);
                log.info("[FullSyncScheduler] brand sync completed");
            }

            // 3) 모델 동기화 (모든 브랜드-카테고리 조합)
            if (modelEnabled) {
                log.info("[FullSyncScheduler] model sync start");
                var pairs = pairProvider.allPairsForModels();
                log.info("[FullSyncScheduler] model pairs = {}", pairs.size());

                for (var pair : pairs) {
                    try {
                        log.info("[FullSyncScheduler] model sync start {}-{}", pair.brandCode(), pair.categoryCode());
                        modelSyncService.syncPair(pair.brandCode(), pair.categoryCode(), modelPageSize);
                    } catch (Exception e) {
                        log.error("[FullSyncScheduler] model sync failed {}-{}", pair.brandCode(), pair.categoryCode(), e);
                    }
                }
                log.info("[FullSyncScheduler] model sync completed");
            }

            log.info("[FullSyncScheduler] all sync completed");
        } catch (Exception e) {
            log.error("[FullSyncScheduler] failed", e);
        }
    }

    /**
     * 테스트용 스케줄러 (JSON 파일 사용)
     * 실제 API가 복구되기 전까지 이 스케줄러를 사용
     */
    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul") // 매일 새벽 3시
    public void runFullSyncWithTestData() {
        log.info("[FullSyncScheduler] start with test data");

        try {
            // 1) 카테고리 동기화
            if (categoryEnabled) {
                log.info("[FullSyncScheduler] category sync with test data start");
                categorySyncService.syncAllWithTestData();
                log.info("[FullSyncScheduler] category sync with test data completed");
            }

            // 2) 브랜드 동기화
            if (brandEnabled) {
                log.info("[FullSyncScheduler] brand sync with test data start");
                brandSyncService.syncAllWithTestData();
                log.info("[FullSyncScheduler] brand sync with test data completed");
            }

            // 3) 모델 동기화 (모든 브랜드-카테고리 조합)
            if (modelEnabled) {
                log.info("[FullSyncScheduler] model sync with test data start");
                var pairs = pairProvider.allPairsForModels();
                log.info("[FullSyncScheduler] model pairs = {}", pairs.size());

                for (var pair : pairs) {
                    try {
                        log.info("[FullSyncScheduler] model sync with test data start {}-{}", pair.brandCode(), pair.categoryCode());
                        modelSyncService.syncPairWithTestData(pair.brandCode(), pair.categoryCode(), modelPageSize);
                    } catch (Exception e) {
                        log.error("[FullSyncScheduler] model sync with test data failed {}-{}", pair.brandCode(), pair.categoryCode(), e);
                    }
                }
                log.info("[FullSyncScheduler] model sync with test data completed");
            }

            log.info("[FullSyncScheduler] all sync with test data completed");
        } catch (Exception e) {
            log.error("[FullSyncScheduler] failed", e);
        }
    }
}
