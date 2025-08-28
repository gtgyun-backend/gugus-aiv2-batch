package com.gugus.batch.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;

@Builder
public record CategorySearchReq(
        Integer page,
        Integer pageSize
) {
    public Map<String, Object> toQueryMap() {
        Map<String, Object> q = new HashMap<>();
        q.put("page", page == null ? 1 : page);
        q.put("pageSize", pageSize == null ? 100 : pageSize);
        return q;
    }
}
