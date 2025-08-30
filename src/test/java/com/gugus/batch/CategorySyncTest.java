package com.gugus.batch;

import com.gugus.batch.service.CategorySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * CategoryScheduler를 테스트 클래스로 변환
 * @author : gtg
 * @date : 2025-08-24
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@RequiredArgsConstructor
public class CategorySyncTest {

    private final CategorySyncService categorySyncService;

    @Value("${sync.category.page-size:200}")
    private int pageSize;

    @Test
    public void syncCategories() {
        log.info("[CategorySyncTest] Category sync job started");

        try {
            categorySyncService.syncAll(pageSize);
            log.info("[CategorySyncTest] Category sync job completed");
        } catch (Exception e) {
            log.error("[CategorySyncTest] Category sync job failed", e);
            throw e;
        }
    }
}
