package com.gugus.batch;

import com.gugus.batch.service.GoodsSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * GoodsScheduler를 테스트 클래스로 변환
 * @author : gtg
 * @date : 2025-08-23
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@RequiredArgsConstructor
public class GoodsSyncTest {

    private final GoodsSyncService goodsSyncService;

    @Value("${sync.goods.page-size:200}")
    private int pageSize;

    @Test
    public void syncGoods() {
        log.info("[GoodsSyncTest] Goods sync job started");

        try {
            goodsSyncService.syncPair("CREATED", "2025-05-01", "2025-05-20", pageSize);
            log.info("[GoodsSyncTest] Goods sync job completed");
        } catch (Exception e) {
            log.error("[GoodsSyncTest] Goods sync job failed", e);
            throw e;
        }
    }
}
