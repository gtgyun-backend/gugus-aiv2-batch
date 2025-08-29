package com.gugus.batch.dto;

import java.util.List;

/**
 * @author : gtg
 * @fileName : GoodsItem
 * @date : 2025-08-23
 */

public record GoodsItem(
        String brandCode,
        String categoryCode,
        String goodsTypeCode,
        String saleStateCode,
        Long goodsNo,
        String goodsName,
        String modelCode,
        String modelName,
        String material,
        String color,
        String origin,
        List<GoodsProperty> goodsProperty,
        String goodsImageUrl,
        String createdAt,
        String modifiedAt
) { 
    public record GoodsProperty(
            String label,
            String value
    ) { }
}

