package com.gugus.batch.scheduler;

import com.gugus.batch.service.GoodsSyncService;
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
public class GoodsScheduler {

    private final GoodsSyncService goodsSyncService;

    @Value("${sync.goods.enabled:true}")
    private boolean enabled;

    @Value("${sync.goods.page-size:200}")
    private int pageSize;

    @Scheduled(cron = "${sync.goods.cron}", zone = "${sync.common.zone:Asia/Seoul}")
    public void run() {
        if (!enabled) return;

        try {
            goodsSyncService.syncPair("CREATED", "2025-05-01", "2025-05-20", pageSize);
        } catch (Exception ignored) {
        }
    }
}
