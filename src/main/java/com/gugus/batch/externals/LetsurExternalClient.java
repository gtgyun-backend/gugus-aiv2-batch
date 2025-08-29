package com.gugus.batch.externals;

import com.gugus.batch.dto.BrandSearchReq;
import com.gugus.batch.dto.BrandsResponse;
import com.gugus.batch.dto.CategoriesResponse;
import com.gugus.batch.dto.CategorySearchReq;
import com.gugus.batch.dto.AppraisalPointSearchReq;
import com.gugus.batch.dto.AppraisalPointsResponse;
import com.gugus.batch.dto.GoodsResponse;
import com.gugus.batch.dto.GoodsSearchReq;
import com.gugus.batch.dto.ModelSearchReq;
import com.gugus.batch.dto.ModelsResponse;
import com.gugus.batch.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author : gtg
 * @fileName : LetsurExternalClient
 * @date : 2025-08-23
 */
@Component
@RequiredArgsConstructor
public class LetsurExternalClient {

    private final WebClientUtil webClientUtil;

    @Value("${externals.letsur.api-key}")
    private String apiKey;

    @Value("${externals.letsur.endpoints.brands}")
    private String brandPath;

    @Value("${externals.letsur.endpoints.categories}")
    private String categoryPath;

    @Value("${externals.letsur.endpoints.management-models}")
    private String modelPath;

    @Value("${externals.letsur.endpoints.goods}")
    private String goodsPath;

    @Value("${externals.letsur.endpoints.appraisal-points}")
    private String appraisalPointsPath;

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-key", apiKey);
        headers.add(HttpHeaders.ACCEPT, "application/json;charset=UTF-8");
        return headers;
    }

    public Mono<BrandsResponse> getBrands(BrandSearchReq req) {
        return webClientUtil.api(HttpMethod.GET, brandPath, req.toQueryMap(),
                BrandsResponse.class, defaultHeaders());
    }

    public Mono<CategoriesResponse> getCategories(CategorySearchReq req) {
        return webClientUtil.api(HttpMethod.GET, categoryPath, req.toQueryMap(),
                CategoriesResponse.class, defaultHeaders());
    }

    public Mono<ModelsResponse> getModels(ModelSearchReq req) {
        return webClientUtil.api(HttpMethod.GET, modelPath, req.toQueryMap(),
                ModelsResponse.class, defaultHeaders());
    }

    public Mono<GoodsResponse> getGoods(GoodsSearchReq req) {
        return webClientUtil.api(HttpMethod.GET, goodsPath, req.toQueryMap(),
                GoodsResponse.class, defaultHeaders());
    }

    public Mono<AppraisalPointsResponse> getAppraisalPoints(AppraisalPointSearchReq req) {
        return webClientUtil.api(HttpMethod.GET, appraisalPointsPath, req.toQueryMap(),
                AppraisalPointsResponse.class, defaultHeaders());
    }
}
