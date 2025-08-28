package com.gugus.batch.dto;

import java.util.List;

public record ModelsResponse(
        List<ModelItem> data,
        Integer page,
        Integer pageSize,
        Integer totalPage,
        Integer totalCount
) { }
