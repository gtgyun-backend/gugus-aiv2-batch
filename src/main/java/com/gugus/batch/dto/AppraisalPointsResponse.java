package com.gugus.batch.dto;

import java.util.List;

/**
 * @author : gtg
 * @fileName : AppraisalPointsResponse
 * @date : 2025-01-27
 */
public record AppraisalPointsResponse(
        List<AppraisalPointItem> data,
        Integer page,
        Integer pageSize,
        Integer totalPage,
        Integer totalCount
) { }
