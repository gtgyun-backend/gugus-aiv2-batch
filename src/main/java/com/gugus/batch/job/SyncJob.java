package com.gugus.batch.job;

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
    private final BrandCategoryPairProvider pairProvider;
    private final GoodsSyncService goodsSyncService;

    /** 1) 카테고리 동기화 → 2) 브랜드 동기화 → 3) 조합별 모델 동기화 */
    public void runAll(int pageSize) {
        // 1) 카테고리 동기화
        log.info("[SyncJob] category sync start");
        categorySyncService.syncAll(pageSize);
        log.info("[SyncJob] category sync completed");

        // 2) 브랜드 동기화
        log.info("[SyncJob] brand sync start");
        brandSyncService.syncAll(pageSize);
        log.info("[SyncJob] brand sync completed");

        // 3) 조합 생성 및 모델 동기화
        var pairs = pairProvider.allPairsForModels();
        log.info("[SyncJob] model pairs = {}", pairs.size());

        for (var p : pairs) {
            try {
                log.info("[SyncJob] model sync start {}-{}", p.brandCode(), p.categoryCode());
                modelSyncService.syncPair(p.brandCode(), p.categoryCode(), pageSize);
            } catch (Exception e) {
                log.error("[SyncJob] model sync failed {}-{}", p.brandCode(), p.categoryCode(), e);
            }
        }
        
        log.info("[SyncJob] all sync completed");
    }

    /** 테스트용: 1) 카테고리 동기화 → 2) 브랜드 동기화 → 3) 조합별 모델 동기화 (JSON 파일 사용) */
    public void runAllWithTestData(int pageSize) {
        // 1) 카테고리 동기화
        log.info("[SyncJob] category sync with test data start");
        categorySyncService.syncAllWithTestData();
        log.info("[SyncJob] category sync with test data completed");

        // 2) 브랜드 동기화
        log.info("[SyncJob] brand sync with test data start");
        brandSyncService.syncAllWithTestData();
        log.info("[SyncJob] brand sync with test data completed");

        // 3) 조합 생성 및 모델 동기화
        var pairs = pairProvider.allPairsForModels();
        log.info("[SyncJob] model pairs = {}", pairs.size());

        for (var p : pairs) {
            try {
                log.info("[SyncJob] model sync with test data start {}-{}", p.brandCode(), p.categoryCode());
                modelSyncService.syncPairWithTestData(p.brandCode(), p.categoryCode(), pageSize);
            } catch (Exception e) {
                log.error("[SyncJob] model sync with test data failed {}-{}", p.brandCode(), p.categoryCode(), e);
            }
        }

//        goodsSyncService.syncPairWithTestData();
        
        log.info("[SyncJob] all sync with test data completed");
    }
}
