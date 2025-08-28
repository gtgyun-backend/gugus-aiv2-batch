-- V2__Add_manual_columns_to_appraisals.sql
-- 감정 테이블에 수동 처리 관련 컬럼들 추가

ALTER TABLE `appraisals` 
ADD COLUMN `purchase_place` varchar(30) DEFAULT NULL COMMENT '구매처' AFTER `model_name_input`,
ADD COLUMN `manual_request_memo` varchar(500) DEFAULT NULL COMMENT '수동 감정 요청 메모' AFTER `purchase_place`,
ADD COLUMN `manual_response_memo` varchar(500) DEFAULT NULL COMMENT '수동 감정 응답 메모' AFTER `manual_request_memo`,
ADD COLUMN `manual_result` varchar(16) DEFAULT NULL COMMENT '수동 감정 처리 결과' AFTER `manual_response_memo`,
ADD COLUMN `manual_status` varchar(16) DEFAULT NULL COMMENT '수동 감정 상태' AFTER `manual_result`,
ADD COLUMN `manual_feedback` varchar(16) DEFAULT NULL COMMENT '수동 감정 처리 피드백' AFTER `manual_status`,
ADD COLUMN `manual_feedback_memo` varchar(50) DEFAULT NULL COMMENT '수동 감정 피드백 메모' AFTER `manual_feedback`;