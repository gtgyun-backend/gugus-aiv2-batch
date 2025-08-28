package com.gugus.batch.dto;

import java.util.List;

public record BrandsResponse(
        List<BrandItem> data,
        Integer page,
        Integer pageSize,
        Integer totalPage,
        Integer totalCount
) { }
