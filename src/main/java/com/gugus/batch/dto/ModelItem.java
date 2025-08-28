package com.gugus.batch.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;

/**
 * @author : gtg
 * @fileName : ManagementModelDtos
 * @date : 2025-08-23
 */

public record ModelItem(
        String modelCode,
        String modelName,
        String createdAt,
        String modifiedAt
) { }

