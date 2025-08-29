package com.gugus.batch.constants;

/**
 * 시스템에서 사용되는 공통 상수들을 정의하는 클래스
 * 
 * @author : system
 * @date : 2025. 1. 27.
 */
public final class SystemConstants {
    
    /**
     * 시스템 사용자 번호 (배치 작업 등에서 사용)
     */
    public static final Long SYSTEM_USER_NO = 0L;
    
    /**
     * 기본 생성자 - 인스턴스화 방지
     */
    private SystemConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
