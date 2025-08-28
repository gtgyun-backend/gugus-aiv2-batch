package com.gugus.batch.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;

@Builder
public record ModelSearchReq(
        String brandCode,      // 필수
        String categoryCode,   // 필수
        Integer page,          // 기본 1
        Integer pageSize       // 기본 100
) {
    @JsonIgnore
    public Map<String, Object> toQueryMap() {
        validate();
        Map<String, Object> q = new HashMap<>();
        q.put("brandCode", brandCode);
        q.put("categoryCode", categoryCode);
        q.put("page", page == null ? 1 : page);
        q.put("pageSize", pageSize == null ? 100 : pageSize);
        return q;
    }

    private void validate() {
        if (brandCode == null || brandCode.isBlank())
            throw new IllegalArgumentException("brandCode is required");
        if (categoryCode == null || categoryCode.isBlank())
            throw new IllegalArgumentException("categoryCode is required");
    }
}
