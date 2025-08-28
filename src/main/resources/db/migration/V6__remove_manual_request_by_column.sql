-- V6__remove_manual_request_by_column.sql
-- appraisals 테이블에서 manual_request_by 컬럼 삭제

-- 먼저 외래키 제약조건 삭제
ALTER TABLE `appraisals` DROP FOREIGN KEY `appraisals_users_FK_2`;

-- manual_request_by 컬럼 삭제
ALTER TABLE `appraisals` DROP COLUMN `manual_request_by`;
