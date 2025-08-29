package com.gugus.batch.dto;

import lombok.Builder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : gtg
 * @fileName : AppraisalPointSearchReq
 * @date : 2025-01-27
 */
@Builder
public record AppraisalPointSearchReq(
        String modelCode
) {
    public Map<String, Object> toQueryMap() {
        Map<String, Object> map = new HashMap<>();
        if (modelCode != null) {
            map.put("modelCode", modelCode);
        }
        return map;
    }
}
