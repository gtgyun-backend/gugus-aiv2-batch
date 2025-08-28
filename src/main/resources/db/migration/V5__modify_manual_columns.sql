-- V5__modify_manual_columns.sql
-- appraisals 테이블에 manual_request_by, manual_response_by 컬럼 추가
-- appraisal_histories 테이블에서 manual 관련 컬럼들 삭제

-- appraisals 테이블에 manual_request_by, manual_response_by 컬럼 추가
ALTER TABLE `appraisals`
ADD COLUMN `manual_request_by` bigint DEFAULT NULL COMMENT '수동 감정 요청자' AFTER `manual_response_at`,
ADD COLUMN `manual_response_by` bigint DEFAULT NULL COMMENT '수동 감정 응답자' AFTER `manual_request_by`;

-- appraisals 테이블의 새로운 컬럼들에 대한 외래키 제약조건 추가
ALTER TABLE `appraisals`
ADD CONSTRAINT `appraisals_users_FK_2` FOREIGN KEY (`manual_request_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT `appraisals_users_FK_3` FOREIGN KEY (`manual_response_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE;

-- appraisal_histories 테이블에서 manual 관련 컬럼들과 제약조건 삭제
-- 먼저 외래키 제약조건 삭제
ALTER TABLE `appraisal_histories` DROP FOREIGN KEY `appraisal_histories_users_FK_2`;

-- manual 관련 컬럼들 삭제
ALTER TABLE `appraisal_histories`
DROP COLUMN `manual_appraisal_result`,
DROP COLUMN `manual_appraisal_status`,
DROP COLUMN `manual_at`,
DROP COLUMN `manual_by`;