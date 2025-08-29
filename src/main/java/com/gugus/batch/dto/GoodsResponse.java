package com.gugus.batch.dto;

import java.util.List;

public record GoodsResponse(
        List<GoodsItem> data,
        Integer page,
        Integer pageSize,
        Integer totalPage,
        Integer totalCount
) { }
