package com.gugus.batch.service;

import com.gugus.batch.database.entities.Brands;
import com.gugus.batch.database.entities.Categories;
import com.gugus.batch.database.entities.Models;
import com.gugus.batch.database.repositories.BrandsRepository;
import com.gugus.batch.database.repositories.CategoriesRepository;
import com.gugus.batch.database.repositories.ModelsRepository;
import com.gugus.batch.externals.LetsurExternalClient;
import com.gugus.batch.dto.ModelItem;
import com.gugus.batch.dto.ModelSearchReq;
import com.gugus.batch.dto.ModelsResponse;
import com.gugus.batch.util.TestDataLoader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * @author : gtg
 * @fileName : ModelSyncService
 * @date : 2025-08-23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ModelSyncService {


    private final LetsurExternalClient client;
    private final ModelsRepository modelsRepository;
    private final BrandsRepository brandsRepository;       // 필요 시 전체 조합 생성용
    private final CategoriesRepository categoriesRepository; // 필요 시 전체 조합 생성용
    private final TestDataLoader testDataLoader;

    @Value("${sync.model.page-size:200}")
    private int defaultPageSize;

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 설정에서 받은 단일 조합 실행 */
    @Transactional
    public void syncPair(String brandCode, String categoryCode, Integer pageSize) {
        log.info("[ModelSyncService] Starting model sync for brandCode={}, categoryCode={}", brandCode, categoryCode);
        
        // 브랜드와 카테고리 정보 조회
        Brands brand = brandsRepository.findByCode(brandCode).orElse(null);
        Categories category = categoriesRepository.findByCode(categoryCode).orElse(null);
        
        if (brand == null || category == null) {
            log.warn("Brand or Category not found: brandCode={}, categoryCode={}", brandCode, categoryCode);
            return;
        }
        
        int page = 1;
        int ps = (pageSize == null ? defaultPageSize : pageSize);
        while (true) {
            ModelsResponse res = client.getModels(
                    ModelSearchReq.builder()
                            .brandCode(brandCode)
                            .categoryCode(categoryCode)
                            .page(page)
                            .pageSize(ps)
                            .build()
            ).onErrorResume(Mono::error).block();

            if (res == null || res.data() == null || res.data().isEmpty()) break;

            upsert(res.data(), brand.getBrandNo(), category.getCategoryNo());
            if (res.page() >= res.totalPage()) break;
            page++;
        }
        
        log.info("[ModelSyncService] Model sync completed for brandCode={}, categoryCode={}", brandCode, categoryCode);
    }

    /**
     * 테스트용 모델 동기화 (JSON 파일 사용)
     */
    @Transactional
    public void syncPairWithTestData(String brandCode, String categoryCode, Integer pageSize) {
        log.info("[ModelSyncService] Starting model sync with test data for brandCode={}, categoryCode={}", brandCode, categoryCode);
        
        // 브랜드와 카테고리 정보 조회
        Brands brand = brandsRepository.findByCode(brandCode).orElse(null);
        Categories category = categoriesRepository.findByCode(categoryCode).orElse(null);
        
        if (brand == null || category == null) {
            log.warn("Brand or Category not found: brandCode={}, categoryCode={}", brandCode, categoryCode);
            return;
        }
        
        // 테스트용 JSON 데이터 로드 (브랜드-카테고리 조합에 관계없이 동일한 데이터 사용)
        ModelsResponse res = testDataLoader.loadModelsResponse();
        log.info("[ModelSyncService] Loaded {} models from test data", res.data().size());
        
        upsert(res.data(), brand.getBrandNo(), category.getCategoryNo());
        log.info("[ModelSyncService] Model sync with test data completed for brandCode={}, categoryCode={}", brandCode, categoryCode);
    }

    /** 전체 브랜드×카테고리 조합을 DB에서 생성해 실행 (옵션) */
    @Transactional
    public void syncAllPairsFromDb() {
        var brands = brandsRepository.findAll();
        var categories = categoriesRepository.findAll();
        for (var b : brands) {
            for (var c : categories) {
                syncPair(b.getCode(), c.getCode(), defaultPageSize);
            }
        }
    }

    @Transactional
    public void upsert(List<ModelItem> items, Integer brandNo, Integer categoryNo) {
        for (var item : items) {
            // 존재여부 판단 키: code
            var entity = modelsRepository.findByCode(item.modelCode()).orElse(null);

            if (entity != null) {
                // 기존 행 → 갱신
                entity.updateNameByBatch(item.modelName(), 1L);
                modelsRepository.save(entity);
            } else {
                // 신규 생성
                var newEntity = Models.createByBatch(item.modelCode(), item.modelName(), brandNo, categoryNo);
                modelsRepository.save(newEntity);
            }
        }
    }
}
