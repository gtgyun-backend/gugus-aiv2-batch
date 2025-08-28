package com.gugus.batch.database.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.gugus.batch.auditlog.enums.AuditActionType;
import com.gugus.batch.util.JsonNodeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author : smk
 * @fileName : AuditLogs
 * @date : 2025. 7. 16.
 */
@Entity
@Getter
@Table(name = "audit_logs")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_no")
    private Long logNo;

    @Column(name = "table_name", length = 64, nullable = false)
    private String tableName;

    @Column(name = "record_id", length = 100, nullable = false)
    private String recordId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", length = 10, nullable = false)
    private AuditActionType actionType;

    @Column(name = "old_values", columnDefinition = "JSON")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode oldValues;

    @Column(name = "new_values", columnDefinition = "JSON")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode newValues;

    @Column(name = "user_no")
    private Long userNo;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", insertable = false, updatable = false)
    private Users user;

    public static AuditLogs create(String tableName, String recordId, AuditActionType actionType,
                                 JsonNode oldValues, JsonNode newValues, Long userNo) {
        AuditLogs log = new AuditLogs();
        log.tableName = tableName;
        log.recordId = recordId;
        log.actionType = actionType;
        log.oldValues = oldValues;
        log.newValues = newValues;
        log.userNo = userNo;
        return log;
    }
} 