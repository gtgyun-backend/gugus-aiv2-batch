package com.gugus.batch.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;

@Builder
public record GoodsSearchReq(
        String dateType,
        String startDate,
        String endDate,
        Integer page,          // 기본 1
        Integer pageSize       // 기본 100
) {

    @JsonIgnore
    public Map<String, Object> toQueryMap() {
        Map<String, Object> q = new HashMap<>();
        q.put("dateType", dateType);
        q.put("startDate", startDate);
        q.put("endDate", endDate);
        q.put("page", page == null ? 1 : page);
        q.put("pageSize", pageSize == null ? 100 : pageSize);
        return q;
    }
}
