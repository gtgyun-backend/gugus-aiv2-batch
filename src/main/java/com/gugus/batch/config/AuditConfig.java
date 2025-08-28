package com.gugus.batch.config;

import com.gugus.batch.auditlog.service.AuditContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * @author : smk
 * @fileName : AuditConfig
 * @date : 2025. 7. 19.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> Optional.ofNullable(AuditContext.getCurrentUserNo());
    }
}
