package com.gugus.batch.dto;

import java.util.List;

public record CategoriesResponse(
        List<CategoryItem> data,
        Integer page,
        Integer pageSize,
        Integer totalPage,
        Integer totalCount
) { }
