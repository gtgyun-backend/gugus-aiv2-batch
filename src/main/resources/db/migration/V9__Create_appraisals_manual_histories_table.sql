-- Create appraisals_manual_histories table for tracking manual appraisal status changes
CREATE TABLE `appraisals_manual_histories` (
   `history_no` bigint NOT NULL AUTO_INCREMENT,
   `appraisal_no` bigint NOT NULL,
   `manual_status` varchar(16) DEFAULT NULL COMMENT '수동 감정 상태 (REQUEST, DONE 등)',
   `manual_result` varchar(16) DEFAULT NULL COMMENT '수동 감정 처리 결과',
   `manual_feedback` varchar(32) DEFAULT NULL COMMENT '수동 감정 처리 피드백',
   `manual_feedback_memo` varchar(50) DEFAULT NULL COMMENT '수동 감정 피드백 메모',
   `manual_request_memo` varchar(500) DEFAULT NULL COMMENT '수동 감정 요청 메모',
   `manual_response_memo` varchar(500) DEFAULT NULL COMMENT '수동 감정 응답 메모',
   `manual_response_by` bigint DEFAULT NULL COMMENT '수동 감정 응답자',
   `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `created_by` bigint DEFAULT NULL,
   PRIMARY KEY (`history_no`),
   KEY `manual_histories_appraisals_FK` (`appraisal_no`),
   KEY `manual_histories_users_FK` (`created_by`),
   KEY `manual_histories_users_FK_1` (`manual_response_by`),
   CONSTRAINT `manual_histories_appraisals_FK` FOREIGN KEY (`appraisal_no`) REFERENCES `appraisals` (`appraisal_no`) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT `manual_histories_users_FK` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE,
   CONSTRAINT `manual_histories_users_FK_1` FOREIGN KEY (`manual_response_by`) REFERENCES `users` (`user_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;