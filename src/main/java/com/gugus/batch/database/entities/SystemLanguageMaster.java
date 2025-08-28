package com.gugus.batch.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author : smk
 * @fileName : SystemLanguageMaster
 * @date : 2025. 7. 19.
 */
@Entity
@Getter
@Table(name = "system_language_master")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemLanguageMaster {

    @EmbeddedId
    private SystemLanguageMasterId id;

    @Column(name = "type", length = 21, nullable = false)
    private String type;

    @Column(name = "content", length = 200, nullable = false)
    private String content;

    @Column(name = "description", length = 50)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
} 