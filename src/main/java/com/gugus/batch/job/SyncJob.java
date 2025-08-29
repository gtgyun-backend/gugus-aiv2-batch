package com.gugus.batch.job;

import com.gugus.batch.service.AppraisalPointSyncService;
import com.gugus.batch.service.BrandCategoryPairProvider;
import com.gugus.batch.service.BrandSyncService;
import com.gugus.batch.service.CategorySyncService;
import com.gugus.batch.service.GoodsSyncService;
import com.gugus.batch.service.ModelSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : gtg
 * @fileName : SyncJob
 * @date : 2025-08-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SyncJob {
    private final BrandSyncService brandSyncService;
    private final CategorySyncService categorySyncService;
    private final ModelSyncService modelSyncService;
    private final AppraisalPointSyncService appraisalPointSyncService;
    private final BrandCategoryPairProvider pairProvider;
    private final GoodsSyncService goodsSyncService;

    /** 1) 카테고리 동기화 → 2) 브랜드 동기화 → 3) 조합별 모델 동기화 */
    public void runAll(int pageSize) {
        log.info("[SyncJob] Starting full sync job");
        
        // 1) 카테고리 동기화
        log.info("[SyncJob] Category sync started");
        categorySyncService.syncAll(pageSize);
        log.info("[SyncJob] Category sync completed");

        // 2) 브랜드 동기화
        log.info("[SyncJob] Brand sync started");
        brandSyncService.syncAll(pageSize);
        log.info("[SyncJob] Brand sync completed");

        // 3) 조합 생성 및 모델 동기화
        var pairs = pairProvider.allPairsForModels();
        log.info("[SyncJob] Processing {} brand-category pairs for model sync", pairs.size());

        int successCount = 0;
        int failCount = 0;
        for (var p : pairs) {
            try {
                modelSyncService.syncPair(p.brandCode(), p.categoryCode(), pageSize);
                successCount++;
            } catch (Exception e) {
                log.error("[SyncJob] Model sync failed for {}-{}", p.brandCode(), p.categoryCode(), e);
                failCount++;
            }
        }
        log.info("[SyncJob] Model sync completed - Success: {}, Failed: {}", successCount, failCount);

        // 4) 감정포인트 동기화 (모든 모델)
        log.info("[SyncJob] Appraisal point sync started");
        appraisalPointSyncService.syncAppraisalPointByModelCode();
        log.info("[SyncJob] Appraisal point sync completed");
        
        log.info("[SyncJob] Full sync job completed successfully");
    }

    /** 테스트용: 1) 카테고리 동기화 → 2) 브랜드 동기화 → 3) 조합별 모델 동기화 (JSON 파일 사용) */
    public void runAllWithTestData(int pageSize) {
        log.info("[SyncJob] Starting full sync job with test data");
        
        // 1) 카테고리 동기화
        log.info("[SyncJob] Category sync with test data started");
        categorySyncService.syncAllWithTestData();
        log.info("[SyncJob] Category sync with test data completed");

        // 2) 브랜드 동기화
        log.info("[SyncJob] Brand sync with test data started");
        brandSyncService.syncAllWithTestData();
        log.info("[SyncJob] Brand sync with test data completed");

        // 3) 조합 생성 및 모델 동기화
        var pairs = pairProvider.allPairsForModels();
        log.info("[SyncJob] Processing {} brand-category pairs for model sync with test data", pairs.size());

        int successCount = 0;
        int failCount = 0;
        for (var p : pairs) {
            try {
                modelSyncService.syncPairWithTestData(p.brandCode(), p.categoryCode(), pageSize);
                successCount++;
            } catch (Exception e) {
                log.error("[SyncJob] Model sync with test data failed for {}-{}", p.brandCode(), p.categoryCode(), e);
                failCount++;
            }
        }
        log.info("[SyncJob] Model sync with test data completed - Success: {}, Failed: {}", successCount, failCount);

        // 4) 감정포인트 동기화 (모든 모델)
        log.info("[SyncJob] Appraisal point sync with test data started");
        try {
            appraisalPointSyncService.syncAppraisalPointWithTestData(null);
            log.info("[SyncJob] Appraisal point sync with test data completed");
        } catch (Exception e) {
            log.error("[SyncJob] Appraisal point sync with test data failed", e);
        }

        log.info("[SyncJob] Full sync job with test data completed successfully");
    }
}
