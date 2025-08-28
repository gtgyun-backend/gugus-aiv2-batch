package com.gugus.batch.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;

/**
 * @author : gtg
 * @fileName : CategoryDtos
 * @date : 2025-08-24
 */
public record CategoryItem(
        String categoryCode,
        String categoryName,
        String createdAt,
        String modifiedAt
) { }

