package com.gugus.batch.auditlog.service;

/**
 * @author : smk
 * @fileName : AuditContext
 * @date : 2025. 7. 16.
 * @description : 감사 로그를 위한 현재 사용자 정보 관리
 */
public class AuditContext {
    private static final ThreadLocal<Long> CURRENT_USER_NO = new ThreadLocal<>();
    public static void setCurrentUserNo(Long userNo) {
        CURRENT_USER_NO.set(userNo);
    }
    public static Long getCurrentUserNo() {
        return CURRENT_USER_NO.get();
    }
    public static void clear() {
        CURRENT_USER_NO.remove();
    }
}
