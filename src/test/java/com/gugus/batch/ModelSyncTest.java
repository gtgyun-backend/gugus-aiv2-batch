package com.gugus.batch;

import com.gugus.batch.service.BrandCategoryPairProvider;
import com.gugus.batch.service.ModelSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * ModelScheduler를 테스트 클래스로 변환
 * @author : gtg
 * @date : 2025-08-23
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@RequiredArgsConstructor
public class ModelSyncTest {

    private final ModelSyncService modelSyncService;
    private final BrandCategoryPairProvider pairProvider;

    @Value("${sync.model.page-size:200}")
    private int pageSize;

    @Test
    public void syncModels() {
        log.info("[ModelSyncTest] Model sync job started");

        try {
            // 모든 브랜드-카테고리 조합에 대해 모델 동기화 실행
            var pairs = pairProvider.allPairsForModels();
            log.info("[ModelSyncTest] Processing {} brand-category pairs", pairs.size());

            int successCount = 0;
            int failCount = 0;
            for (var pair : pairs) {
                try {
                    modelSyncService.syncPair(pair.brandCode(), pair.categoryCode(), pageSize);
                    successCount++;
                } catch (Exception e) {
                    log.error("[ModelSyncTest] Model sync failed for {}-{}", pair.brandCode(), pair.categoryCode(), e);
                    failCount++;
                }
            }
            
            log.info("[ModelSyncTest] Model sync job completed - Success: {}, Failed: {}", successCount, failCount);
        } catch (Exception e) {
            log.error("[ModelSyncTest] Model sync job failed", e);
            throw e;
        }
    }
}
