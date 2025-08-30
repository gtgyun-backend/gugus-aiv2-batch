package com.gugus.batch;

import com.gugus.batch.service.BrandSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * BrandScheduler를 테스트 클래스로 변환
 * @author : gtg
 * @date : 2025-08-24
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@RequiredArgsConstructor
public class BrandSyncTest {

    private final BrandSyncService brandSyncService;

    @Value("${sync.brand.page-size:200}")
    private int pageSize;

    @Test
    public void syncBrands() {
        log.info("[BrandSyncTest] Brand sync job started");

        try {
            brandSyncService.syncAll(pageSize);
            log.info("[BrandSyncTest] Brand sync job completed");
        } catch (Exception e) {
            log.error("[BrandSyncTest] Brand sync job failed", e);
            throw e;
        }
    }
}
