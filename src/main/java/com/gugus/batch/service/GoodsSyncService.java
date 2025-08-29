package com.gugus.batch.service;

import com.gugus.batch.database.entities.ColorChips;
import com.gugus.batch.database.entities.Goods;
import com.gugus.batch.database.entities.GoodsProperty;
import com.gugus.batch.database.entities.Materials;
import com.gugus.batch.database.entities.OriginCountries;
import com.gugus.batch.database.repositories.ColorChipsRepository;
import com.gugus.batch.database.repositories.GoodsPropertyRepository;
import com.gugus.batch.database.repositories.GoodsRepository;
import com.gugus.batch.database.repositories.MaterialsRepository;
import com.gugus.batch.database.repositories.OriginCountriesRepository;
import com.gugus.batch.dto.GoodsItem;
import com.gugus.batch.dto.GoodsResponse;
import com.gugus.batch.dto.GoodsSearchReq;
import com.gugus.batch.externals.LetsurExternalClient;
import com.gugus.batch.util.TestDataLoader;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * @author : gtg
 * @fileName : GoodsSyncService
 * @date : 2025-08-29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GoodsSyncService {


    private final OriginCountriesRepository originCountriesRepository;
    private final GoodsPropertyRepository goodsPropertyRepository;
    private final ColorChipsRepository colorChipsRepository;
    private final MaterialsRepository materialsRepository;
    private final GoodsRepository goodsRepository;
    private final TestDataLoader testDataLoader;
    private final LetsurExternalClient client;
    private final EntityManager entityManager;

    @Value("${sync.model.page-size:200}")
    private int defaultPageSize;

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 설정에서 받은 단일 조합 실행
     */
    @Transactional
    public void syncPair(String dateType, String startDate, String endDate, Integer pageSize) {
        log.info("[GoodsSyncService] Starting goods sync for dateType={}, startDate={}, endDate={}", 
                dateType, startDate, endDate);
        
        // ID만 추출한 Map 생성
        var originNoMap = originCountriesRepository.findAllByOrderByListOrderAsc()
                .stream()
                .collect(Collectors.toMap(OriginCountries::getName, OriginCountries::getOriginNo));

        var colorNoMap = colorChipsRepository.findAllByOrderByListOrderAsc()
                .stream()
                .collect(Collectors.toMap(ColorChips::getName, ColorChips::getColorNo));

        var materialNoMap = materialsRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Materials::getName, Materials::getMaterialNo));
        
        int page = 1;
        int ps = (pageSize == null ? defaultPageSize : pageSize);
        int totalProcessed = 0;
        
        while (true) {
            GoodsResponse res = client.getGoods(
                    GoodsSearchReq.builder()
                            .dateType(dateType)
                            .startDate(startDate)
                            .endDate(endDate)
                            .page(page)
                            .pageSize(ps)
                            .build()
            ).onErrorResume(Mono::error).block();

            if (res == null || res.data() == null || res.data().isEmpty()) {
                break;
            }

            upsert(res.data(), originNoMap, colorNoMap, materialNoMap);
            totalProcessed += res.data().size();
            
            if (res.page() >= res.totalPage()) {
                break;
            }
            page++;
        }
        
        log.info("[GoodsSyncService] Goods sync completed - Total processed: {}", totalProcessed);
    }

    /**
     * 테스트용 모델 동기화 (JSON 파일 사용)
     */
    @Transactional
    public void syncPairWithTestData() {
        log.info("[GoodsSyncService] Starting goods sync with test data");
        
        GoodsResponse res = testDataLoader.loadGoodsResponse();
        var originNoMap = originCountriesRepository.findAllByOrderByListOrderAsc()
                .stream()
                .collect(Collectors.toMap(OriginCountries::getName, OriginCountries::getOriginNo));

        var colorNoMap = colorChipsRepository.findAllByOrderByListOrderAsc()
                .stream()
                .collect(Collectors.toMap(ColorChips::getName, ColorChips::getColorNo));

        var materialNoMap = materialsRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Materials::getName, Materials::getMaterialNo));
        
        upsert(res.data(), originNoMap, colorNoMap, materialNoMap);
        log.info("[GoodsSyncService] Goods sync with test data completed - Processed: {}", res.data().size());
    }

    @Transactional
    public void upsert(List<GoodsItem> items, Map<String, Integer> originNoMap, Map<String, Integer> colorNoMap, Map<String, Integer> materialNoMap) {
        int createdCount = 0;
        int updatedCount = 0;
        int errorCount = 0;
        
        for (var item : items) {
            try {
                var entity = goodsRepository.findByLegacyGoodsNo(item.goodsNo()).orElse(null);
                var materialNo = materialNoMap.get(item.material());
                var colorNo = colorNoMap.get(item.color());
                var originNo = originNoMap.get(item.origin());
                
                if (entity == null) {
                    LocalDateTime createdAt = LocalDateTime.parse(item.createdAt(), DF);
                    LocalDateTime updatedAt = LocalDateTime.parse(item.modifiedAt(), DF);
                    var newEntity = Goods.createByBatch(item, materialNo, colorNo, originNo, createdAt, updatedAt);
                    goodsRepository.save(newEntity);
                    if (item.goodsProperty() != null && !item.goodsProperty().isEmpty()) {
                        for (var property : item.goodsProperty()) {
                            goodsPropertyRepository.save(GoodsProperty.createByBatch(newEntity, property, 1L));
                        }
                    }
                    createdCount++;
                } else {
                    LocalDateTime createdAt = LocalDateTime.parse(item.createdAt(), DF);
                    LocalDateTime updatedAt = LocalDateTime.parse(item.modifiedAt(), DF);
                    entity.updateNameByBatch(item, materialNo, colorNo, originNo, 1L, createdAt, updatedAt);
                    goodsRepository.save(entity);
                    updatedCount++;
                }
                entityManager.flush();
                entityManager.clear();
            } catch (Exception e) {
                log.error("[GoodsSyncService] Error during upsert for goods: goodsNo={}", item.goodsNo(), e);
                errorCount++;
            }
        }
        
        if (createdCount > 0 || updatedCount > 0 || errorCount > 0) {
            log.info("[GoodsSyncService] Upsert completed - Created: {}, Updated: {}, Errors: {}", 
                    createdCount, updatedCount, errorCount);
        }
    }
}
