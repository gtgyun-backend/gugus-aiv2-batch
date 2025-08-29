package com.gugus.batch.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gugus.batch.dto.AppraisalPointsResponse;
import com.gugus.batch.dto.BrandsResponse;
import com.gugus.batch.dto.CategoriesResponse;
import com.gugus.batch.dto.GoodsResponse;
import com.gugus.batch.dto.ModelsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.ClassPathResource;

/**
 * 테스트용 JSON 데이터를 로드하는 유틸리티 클래스
 * 실제 API 호출이 불가능한 상황에서 테스트용으로 사용
 */
@Slf4j
@Component
public class TestDataLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CategoriesResponse loadCategoriesResponse() {
        try {
            ClassPathResource resource = new ClassPathResource("real-data/20250830/categories.json");
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, CategoriesResponse.class);
        } catch (IOException e) {
            log.error("Failed to load categories test data", e);
            throw new RuntimeException("Failed to load categories test data", e);
        }
    }

    public BrandsResponse loadBrandsResponse() {
        try {
            ClassPathResource resource = new ClassPathResource("real-data/20250830/brands.json");
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, BrandsResponse.class);
        } catch (IOException e) {
            log.error("Failed to load brands test data", e);
            throw new RuntimeException("Failed to load brands test data", e);
        }
    }

    public ModelsResponse loadModelsResponse() {
        try {
            ClassPathResource resource = new ClassPathResource("real-data/20250830/models.json");
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, ModelsResponse.class);
        } catch (IOException e) {
            log.error("Failed to load models test data", e);
            throw new RuntimeException("Failed to load models test data", e);
        }
    }

    public AppraisalPointsResponse loadAppraisalPointsResponse() {
        try {
            ClassPathResource resource = new ClassPathResource("real-data/20250830/appraisal_points.json");
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, AppraisalPointsResponse.class);
        } catch (IOException e) {
            log.error("Failed to load appraisal points test data", e);
            throw new RuntimeException("Failed to load appraisal points test data", e);
        }
    }

    public GoodsResponse loadGoodsResponse() {
        try {
            ClassPathResource resource = new ClassPathResource("test-data/goods-response.json");
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, GoodsResponse.class);
        } catch (IOException e) {
            log.error("Failed to load goods test data", e);
            throw new RuntimeException("Failed to load goods test data", e);
        }
    }

}
