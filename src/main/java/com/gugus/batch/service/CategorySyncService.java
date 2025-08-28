package com.gugus.batch.service;

import com.gugus.batch.database.entities.Categories;
import com.gugus.batch.database.repositories.CategoriesRepository;
import com.gugus.batch.dto.CategoriesResponse;
import com.gugus.batch.dto.CategoryItem;
import com.gugus.batch.dto.CategorySearchReq;
import com.gugus.batch.externals.LetsurExternalClient;
import com.gugus.batch.util.TestDataLoader;

import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : gtg
 * @fileName : CategorySyncService
 * @date : 2025-08-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategorySyncService {

    private final LetsurExternalClient client;
    private final CategoriesRepository categoriesRepository;
    private final TestDataLoader testDataLoader;

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public void syncAll(int pageSize) {
        log.info("[CategorySyncService] Starting category sync");
        
        int page = 1;
        while (true) {
            CategoriesResponse res = client.getCategories(
                    CategorySearchReq.builder().page(page).pageSize(pageSize).build()
            ).onErrorResume(Mono::error).block();
            if (res == null || res.data() == null || res.data().isEmpty()) break;

            upsert(res.data());
            if (res.page() >= res.totalPage()) break;
            page++;
        }
        
        log.info("[CategorySyncService] Category sync completed");
    }

    /**
     * 테스트용 카테고리 동기화 (JSON 파일 사용)
     */
    @Transactional
    public void syncAllWithTestData() {
        log.info("[CategorySyncService] Starting category sync with test data");
        
        CategoriesResponse res = testDataLoader.loadCategoriesResponse();
        log.info("[CategorySyncService] Loaded {} categories from test data", res.data().size());
        
        upsert(res.data());
        log.info("[CategorySyncService] Category sync with test data completed");
    }

    private void upsert(List<CategoryItem> items) {
        for (var item : items) {
            // categoryCode가 null인 경우 건너뛰기
            if (item.categoryCode() == null) {
                continue;
            }
            
            var entity = categoriesRepository.findByCode(item.categoryCode()).orElse(null);

            if (entity != null) {
                // 기존 행 → 갱신
                    entity.updateNameByBatch(item.categoryName());
                categoriesRepository.save(entity);
            } else {
                // 신규 생성
                var newEntity = Categories.createByBatch(item.categoryCode(), item.categoryName());
                categoriesRepository.save(newEntity);
            }            
        }
    }

}