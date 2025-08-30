package com.gugus.batch.service;

import com.gugus.batch.database.entities.AppraisalPoints;
import com.gugus.batch.database.entities.ProductModels;
import com.gugus.batch.database.repositories.AppraisalPointsRepository;
import com.gugus.batch.database.repositories.ProductModelsRepository;
import com.gugus.batch.dto.AppraisalPointItem;
import com.gugus.batch.dto.AppraisalPointSearchReq;
import com.gugus.batch.dto.AppraisalPointsResponse;
import com.gugus.batch.externals.LetsurExternalClient;
import com.gugus.batch.util.TestDataLoader;
import jakarta.persistence.EntityManager;
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
 * @fileName : AppraisalPointSyncService
 * @date : 2025-01-27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AppraisalPointSyncService {

    private final LetsurExternalClient client;
    private final AppraisalPointsRepository appraisalPointsRepository;
    private final ProductModelsRepository productModelsRepository;
    private final TestDataLoader testDataLoader;
    private final EntityManager entityManager;

    @Value("${sync.appraisal-point.page-size:200}")
    private int defaultPageSize;

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 특정 모델의 감정포인트 동기화 */
    @Transactional
    public void syncAppraisalPointByModelCode() {
        log.info("[AppraisalPointSyncService] Starting appraisal point sync");
        
        // 모델 정보 조회
        List<ProductModels> models = productModelsRepository.findAll();
        log.info("[AppraisalPointSyncService] Found {} models to process", models.size());
        
        int processedCount = 0;
        int errorCount = 0;
        
        for (ProductModels model : models) {
             // API에서 감정포인트 데이터 조회
            AppraisalPointsResponse res = client.getAppraisalPoints(
                AppraisalPointSearchReq.builder()
                        .modelCode(model.getCode())
                        .build()
            ).onErrorResume(Mono::error).block();

        if (res == null || res.data() == null || res.data().isEmpty()) {
            continue;
        }

        try {
            upsert(res.data(), model.getProductModelNo());
            processedCount++;
        } catch (Exception e) {
            log.error("[AppraisalPointSyncService] Failed to process modelCode={}", model.getCode(), e);
            errorCount++;
        }
        }
        
        log.info("[AppraisalPointSyncService] Appraisal point sync completed - Processed: {}, Errors: {}", 
                processedCount, errorCount);
    }

    /**
     * 테스트용 감정포인트 동기화 (JSON 파일 사용)
     */
    @Transactional
    public void syncAppraisalPointWithTestData() {
        log.info("[AppraisalPointSyncService] Starting appraisal point sync with test data");
        
        // 모델 정보 조회
        List<ProductModels> models = productModelsRepository.findAll();
        
        // 테스트용 JSON 데이터 로드
        AppraisalPointsResponse res = testDataLoader.loadAppraisalPointsResponse();
        log.info("[AppraisalPointSyncService] Loaded {} appraisal points from test data", res.data().size());

        final int[] processedCount = {0};
        final int[] errorCount = {0};

        res.data().forEach(item -> {
            ProductModels model = models.stream()
                    .filter(m -> m.getCode().equals(item.modelCode()))
                    .findFirst()
                    .orElse(null);

            if (model != null) {
                try {
                    // Create a list with just this item for the specific model
                    List<AppraisalPointItem> singleItemList = List.of(item);
                    upsert(singleItemList, model.getProductModelNo());
                    processedCount[0]++;
                } catch (Exception e) {
                    log.error("[AppraisalPointSyncService] Failed to process test data for modelCode={}", item.modelCode(), e);
                    errorCount[0]++;
                }
            }
        });
        
        log.info("[AppraisalPointSyncService] Appraisal point sync with test data completed - Processed: {}, Errors: {}", 
                processedCount[0], errorCount[0]);        
    }

    private void upsert(List<AppraisalPointItem> items, Long modelNo) {
        int createdCount = 0;
        int updatedCount = 0;
        
        for (int i = 0; i < items.size(); i++) {
            AppraisalPointItem item = items.get(i);
            try {
                // 1. AppraisalPoints 테이블에 기본 감정포인트 정보 저장/업데이트
                AppraisalPoints existingPoint = appraisalPointsRepository
                        .findByName(item.pointName())
                        .orElse(null);

                Long pointNo;
                if (existingPoint == null) {
                    // 새로 생성
                    AppraisalPoints newPoint = AppraisalPoints.createByBatch(item);
                    AppraisalPoints savedPoint = appraisalPointsRepository.save(newPoint);
                    pointNo = savedPoint.getPointNo(); // 저장 후 생성된 ID 사용
                    createdCount++;
                } else {
                    // 기존 데이터 업데이트
                    existingPoint.updateByBatch(item);
                    appraisalPointsRepository.save(existingPoint);
                    pointNo = existingPoint.getPointNo(); // 기존 ID 사용
                    updatedCount++;
                }

                // 영속성 컨텍스트 메모리 최적화
                entityManager.flush();
                entityManager.clear();
                
            } catch (Exception e) {
                log.error("[AppraisalPointSyncService] Failed to upsert appraisal point: pointNo={}, modelNo={}", 
                        item.pointNo(), modelNo, e);
                throw e;
            }
        }
        
        if (createdCount > 0 || updatedCount > 0) {
            log.info("[AppraisalPointSyncService] Upsert completed for modelNo={} - Created: {}, Updated: {}", 
                    modelNo, createdCount, updatedCount);
        }
    }
}
