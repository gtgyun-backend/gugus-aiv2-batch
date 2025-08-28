-- V4__modify_manual_feedback_column_size.sql

ALTER TABLE `appraisals` 
MODIFY COLUMN `manual_feedback` varchar(32) DEFAULT NULL COMMENT '수동 감정 처리 피드백';