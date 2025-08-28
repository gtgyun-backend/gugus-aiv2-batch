package com.gugus.batch.service;

import com.gugus.batch.database.repositories.BrandsRepository;
import com.gugus.batch.database.repositories.CategoriesRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author : gtg
 * @fileName : BrandCategoryPairProvider
 * @date : 2025-08-24
 */
@Component
@RequiredArgsConstructor
public class BrandCategoryPairProvider {

    private final BrandsRepository brandsRepository;
    private final CategoriesRepository categoriesRepository;

    /** categoryCode 가 null 인 항목은 모델 API 대상에서 제외 */
    public List<Pair> allPairsForModels() {
        var brands = brandsRepository.findAll();         // brandCode 필수
        var categories = categoriesRepository.findAll();  // categoryCode null 있을 수 있음

        var result = new ArrayList<Pair>();
        for (var b : brands) {
            if (b.getCode() == null || b.getCode().isBlank()) continue;
            for (var c : categories) {
                if (c.getCode() == null || c.getCode().isBlank()) continue; // null 제외
                result.add(new Pair(b.getCode(), c.getCode()));
            }
        }
        return result;
    }

    public record Pair(String brandCode, String categoryCode) {}
}
