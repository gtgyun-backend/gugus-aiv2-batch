package com.gugus.batch.auditlog.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : smk
 * @fileName : Auditable
 * @date : 2025. 7. 19.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    /**
     * 감사 로그에서 사용할 테이블명 (기본값: 엔티티의 @Table name)
     */
    String tableName() default "";

    /**
     * 제외할 필드명들 (민감한 정보나 불필요한 필드 제외)
     */
    String[] excludeFields() default {};
}
