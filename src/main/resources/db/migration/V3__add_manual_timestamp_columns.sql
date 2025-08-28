-- V3__add_manual_timestamp_columns.sql

ALTER TABLE `appraisals`
ADD COLUMN `manual_request_at` timestamp NULL DEFAULT NULL COMMENT '수동 감정 요청 시간' AFTER `purchase_place`,
ADD COLUMN `manual_response_at` timestamp NULL DEFAULT NULL COMMENT '수동 감정 응답 시간' AFTER `manual_request_at`;