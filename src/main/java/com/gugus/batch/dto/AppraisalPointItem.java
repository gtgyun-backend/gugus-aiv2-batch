package com.gugus.batch.dto;

/**
 * @author : gtg
 * @fileName : AppraisalPointItem
 * @date : 2025-01-27
 */
public record AppraisalPointItem(
        String modelCode,
        String pointNo,
        String pointName,
        Integer imageCount,
        String guideImageUrl,
        String mandatoryYn,
        String createdAt,
        String modifiedAt
) { }
