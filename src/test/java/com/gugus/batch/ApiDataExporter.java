package com.gugus.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gugus.batch.dto.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * DB 연결 없이 API 호출 결과를 JSON 파일로 저장하는 테스트 클래스
 */
public class ApiDataExporter {

    private static final String BASE_URL = "https://ggsapi.gugus.co.kr/api";
    private static final String API_KEY = System.getenv("LETSUR_API_KEY"); // 환경변수에서 API 키 가져오기
    
    private static final WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .build();
    
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void main(String[] args) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.err.println("LETSUR_API_KEY 환경변수가 설정되지 않았습니다.");
            System.exit(1);
        }

        System.out.println("API 데이터 수집을 시작합니다...");
        
        try {
            // 출력 디렉토리 생성
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String outputDir = "src/main/resources/real-data/" + timestamp;
            Files.createDirectories(Paths.get(outputDir));
            
            // 1. 카테고리 데이터 수집
            System.out.println("카테고리 데이터를 수집합니다...");
            exportCategories(outputDir);
            
            // 2. 브랜드 데이터 수집
            System.out.println("브랜드 데이터를 수집합니다...");
            exportBrands(outputDir);
            
            // 3. 모델 데이터 수집
            System.out.println("모델 데이터를 수집합니다...");
            exportModels(outputDir);
            
            // 4. 감정포인트 데이터 수집
            System.out.println("감정포인트 데이터를 수집합니다...");
            exportAppraisalPoints(outputDir);
            
            System.out.println("모든 데이터 수집이 완료되었습니다. 결과는 " + outputDir + " 폴더에 저장되었습니다.");
            
        } catch (Exception e) {
            System.err.println("데이터 수집 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void exportCategories(String outputDir) {
        try {
            String endpoint = "/v1/externals/letsur/category";
            Map<String, Object> params = new HashMap<>();
            params.put("page", 1);
            params.put("pageSize", 200);
            
            CategoriesResponse response = callApi(endpoint, params, CategoriesResponse.class);
            
            if (response != null) {
                String filename = outputDir + "/categories.json";
                objectMapper.writeValue(new File(filename), response);
                System.out.println("카테고리 데이터가 " + filename + "에 저장되었습니다. (총 " + response.data().size() + "개)");
            }
            
        } catch (Exception e) {
            System.err.println("카테고리 데이터 수집 실패: " + e.getMessage());
        }
    }

    private static void exportBrands(String outputDir) {
        try {
            String endpoint = "/v1/externals/letsur/brand";
            Map<String, Object> params = new HashMap<>();
            params.put("page", 1);
            params.put("pageSize", 200);
            
            BrandsResponse response = callApi(endpoint, params, BrandsResponse.class);
            
            if (response != null) {
                String filename = outputDir + "/brands.json";
                objectMapper.writeValue(new File(filename), response);
                System.out.println("브랜드 데이터가 " + filename + "에 저장되었습니다. (총 " + response.data().size() + "개)");
            }
            
        } catch (Exception e) {
            System.err.println("브랜드 데이터 수집 실패: " + e.getMessage());
        }
    }

    private static void exportModels(String outputDir) {
        try {
            // 카테고리와 브랜드 데이터를 먼저 읽어옴
            CategoriesResponse categoriesResponse = objectMapper.readValue(
                new File(outputDir + "/categories.json"), CategoriesResponse.class);
            BrandsResponse brandsResponse = objectMapper.readValue(
                new File(outputDir + "/brands.json"), BrandsResponse.class);
            
            System.out.println("카테고리 " + categoriesResponse.data().size() + "개, 브랜드 " + brandsResponse.data().size() + "개 조합으로 모델을 수집합니다...");
            
            // 모든 카테고리-브랜드 조합에 대해 모델 수집
            int totalModels = 0;
            int successCount = 0;
            int failCount = 0;
            java.util.List<ModelItem> allModels = new java.util.ArrayList<>();
            
            for (CategoryItem category : categoriesResponse.data()) {
                for (BrandItem brand : brandsResponse.data()) {
                    try {
                        String endpoint = "/v1/externals/letsur/management-models";
                        Map<String, Object> params = new HashMap<>();
                        params.put("brandCode", brand.brandCode());
                        params.put("categoryCode", category.categoryCode());
                        params.put("page", 1);
                        params.put("pageSize", 200);
                        
                        ModelsResponse response = callApi(endpoint, params, ModelsResponse.class);
                        
                        if (response != null && !response.data().isEmpty()) {
                            allModels.addAll(response.data());
                            totalModels += response.data().size();
                            successCount++;
                            System.out.println(String.format("모델 수집 성공: 카테고리[%s] 브랜드[%s] - %d개", 
                                category.categoryName(), brand.brandName(), response.data().size()));
                        } else {
                            failCount++;
                            System.out.println(String.format("모델 없음: 카테고리[%s] 브랜드[%s]", 
                                category.categoryName(), brand.brandName()));
                        }
                        
                        // API 호출 간격 조절 (새벽 시간대 서버 부하 방지)
                        Thread.sleep(500);
                        
                    } catch (Exception e) {
                        failCount++;
                        System.err.println(String.format("모델 수집 실패: 카테고리[%s] 브랜드[%s] - %s", 
                            category.categoryName(), brand.brandName(), e.getMessage()));
                    }
                }
            }
            
            // 요약 정보 저장
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalCombinations", categoriesResponse.data().size() * brandsResponse.data().size());
            summary.put("successCount", successCount);
            summary.put("failCount", failCount);
            summary.put("totalModels", totalModels);
            summary.put("categories", categoriesResponse.data().size());
            summary.put("brands", brandsResponse.data().size());
            
            objectMapper.writeValue(new File(outputDir + "/models_summary.json"), summary);
            System.out.println(String.format("모델 수집 완료: 성공 %d개, 실패 %d개, 총 모델 %d개", 
                successCount, failCount, totalModels));
            
            // 모든 모델 데이터를 하나의 파일로 저장
            ModelsResponse consolidatedResponse = new ModelsResponse(allModels, 1, allModels.size(), 1, allModels.size());
            objectMapper.writeValue(new File(outputDir + "/models.json"), consolidatedResponse);
            System.out.println("모든 모델 데이터가 통합되어 models.json에 저장되었습니다. (총 " + allModels.size() + "개)");
            
        } catch (Exception e) {
            System.err.println("모델 데이터 수집 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private static void exportAppraisalPoints(String outputDir) {
        try {
            // 기존 모델 데이터를 읽어옴
            File allModelsFile = new File(outputDir + "/models.json");
            if (!allModelsFile.exists()) {
                System.err.println("모델 데이터가 없습니다. 먼저 모델 데이터를 수집해주세요.");
                return;
            }
            
            ModelsResponse allModelsResponse = objectMapper.readValue(allModelsFile, ModelsResponse.class);
            System.out.println("총 " + allModelsResponse.data().size() + "개 모델에 대해 감정포인트를 수집합니다...");
            
            int totalPoints = 0;
            int successCount = 0;
            int failCount = 0;
            java.util.List<AppraisalPointItem> allPoints = new java.util.ArrayList<>();
            
            for (ModelItem model : allModelsResponse.data()) {
                try {
                    String endpoint = "/v1/externals/letsur/appraisal-point";
                    Map<String, Object> params = new HashMap<>();
                    params.put("modelCode", model.modelCode());
                    
                    AppraisalPointsResponse response = callApi(endpoint, params, AppraisalPointsResponse.class);
                    
                    if (response != null && !response.data().isEmpty()) {
                        // modelCode를 포함한 감정포인트 데이터 생성
                        for (AppraisalPointItem point : response.data()) {
                            AppraisalPointItem pointWithModelCode = new AppraisalPointItem(
                                model.modelCode(),
                                point.pointNo(),
                                point.pointName(),
                                point.imageCount(),
                                point.guideImageUrl(),
                                point.mandatoryYn(),
                                point.createdAt(),
                                point.modifiedAt()
                            );
                            allPoints.add(pointWithModelCode);
                        }
                        totalPoints += response.data().size();
                        successCount++;
                        System.out.println(String.format("감정포인트 수집 성공: 모델[%s] - %d개", 
                            model.modelName(), response.data().size()));
                    } else {
                        failCount++;
                        System.out.println(String.format("감정포인트 없음: 모델[%s]", 
                            model.modelName()));
                    }
                    
                    // API 호출 간격 조절 (새벽 시간대 서버 부하 방지)
                    Thread.sleep(500);
                    
                } catch (Exception e) {
                    failCount++;
                    System.err.println(String.format("감정포인트 수집 실패: 모델[%s] - %s", 
                        model.modelName(), e.getMessage()));
                }
            }
            
            // 요약 정보 저장
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalModels", allModelsResponse.data().size());
            summary.put("successCount", successCount);
            summary.put("failCount", failCount);
            summary.put("totalPoints", totalPoints);
            
            objectMapper.writeValue(new File(outputDir + "/appraisal_points_summary.json"), summary);
            System.out.println(String.format("감정포인트 수집 완료: 성공 %d개, 실패 %d개, 총 감정포인트 %d개", 
                successCount, failCount, totalPoints));
            
            // 모든 감정포인트 데이터를 하나의 파일로 저장
            AppraisalPointsResponse consolidatedResponse = new AppraisalPointsResponse(allPoints, 1, allPoints.size(), 1, allPoints.size());
            objectMapper.writeValue(new File(outputDir + "/appraisal_points.json"), consolidatedResponse);
            System.out.println("모든 감정포인트 데이터가 통합되어 appraisal_points.json에 저장되었습니다. (총 " + allPoints.size() + "개)");
            
        } catch (Exception e) {
            System.err.println("감정포인트 데이터 수집 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private static <T> T callApi(String endpoint, Map<String, Object> params, Class<T> responseType) {
        try {
            // 쿼리 파라미터 구성
            StringBuilder queryString = new StringBuilder();
            if (params != null && !params.isEmpty()) {
                queryString.append("?");
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (queryString.length() > 1) {
                        queryString.append("&");
                    }
                    queryString.append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
            
            String url = endpoint + queryString.toString();
            
            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add("x-api-key", API_KEY);
            headers.add(HttpHeaders.ACCEPT, "application/json;charset=UTF-8");
            
            // API 호출
            return webClient.method(HttpMethod.GET)
                    .uri(url)
                    .headers(h -> h.addAll(headers))
                    .retrieve()
                    .bodyToMono(responseType)
                    .block(); // 동기 호출로 변경
                    
        } catch (Exception e) {
            System.err.println("API 호출 실패 (" + endpoint + "): " + e.getMessage());
            return null;
        }
    }
}
