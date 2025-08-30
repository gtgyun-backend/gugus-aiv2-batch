package com.gugus.batch.service;

import com.gugus.batch.database.entities.Brands;
import com.gugus.batch.database.repositories.BrandsRepository;
import com.gugus.batch.externals.LetsurExternalClient;
import com.gugus.batch.dto.BrandItem;
import com.gugus.batch.dto.BrandSearchReq;
import com.gugus.batch.dto.BrandsResponse;
import com.gugus.batch.util.TestDataLoader;
import jakarta.persistence.EntityManager;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * @author : gtg
 * @fileName : BrandSyncService
 * @date : 2025-08-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BrandSyncService {

    private final LetsurExternalClient client;
    private final BrandsRepository brandsRepository;
    private final TestDataLoader testDataLoader;
    private final EntityManager entityManager;

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public void syncAll(int pageSize) {
        log.info("[BrandSyncService] Starting brand sync");
        
        int page = 1;
        int totalProcessed = 0;
        
        while (true) {
            BrandsResponse res = client.getBrands(
                    BrandSearchReq.builder().page(page).pageSize(pageSize).build()
            ).onErrorResume(Mono::error).block();
            if (res == null || res.data() == null || res.data().isEmpty()) break;

            upsert(res.data());
            totalProcessed += res.data().size();
            
            if (res.page() >= res.totalPage()) break;
            page++;
        }
        
        log.info("[BrandSyncService] Brand sync completed - Total processed: {}", totalProcessed);
    }

    /**
     * 테스트용 브랜드 동기화 (JSON 파일 사용)
     */
    @Transactional
    public void syncAllWithTestData() {
        log.info("[BrandSyncService] Starting brand sync with test data");
        
        BrandsResponse res = testDataLoader.loadBrandsResponse();
        log.info("[BrandSyncService] Loaded {} brands from test data", res.data().size());
        
        upsert(res.data());
        log.info("[BrandSyncService] Brand sync with test data completed - Processed: {}", res.data().size());
    }

    private void upsert(List<BrandItem> items) {
        int createdCount = 0;
        int updatedCount = 0;
        
        for (var item : items) {
            var entity = brandsRepository.findByCode(item.brandCode()).orElse(null);

            if (entity != null) {
                // 기존 행 → 갱신
                entity.updateNameByBatch(item.brandName());
                brandsRepository.save(entity);
                updatedCount++;
            } else {
                // 신규 생성
                var newEntity = Brands.createByBatch(item.brandCode(), item.brandName());
                brandsRepository.save(newEntity);
                createdCount++;
            }
            
            // 영속성 컨텍스트 메모리 최적화
            entityManager.flush();
            entityManager.clear();
        }
        
        if (createdCount > 0 || updatedCount > 0) {
            log.info("[BrandSyncService] Upsert completed - Created: {}, Updated: {}", createdCount, updatedCount);
        }
    }

}