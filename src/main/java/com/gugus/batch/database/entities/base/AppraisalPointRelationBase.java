package com.gugus.batch.database.entities.base;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.gugus.batch.enums.AppraisalUseType;

import java.time.LocalDateTime;

/**
 * @author : smk
 * @fileName : AppraisalPointRelationBase
 * @date : 2025. 7. 16.
 */
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AppraisalPointRelationBase implements Serializable {

    @Column(name = "required", nullable = false)
    protected Boolean required = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "use_type", length = 10, nullable = false)
    protected AppraisalUseType useType = AppraisalUseType.USE;

    @Column(name = "image_count", nullable = false)
    protected Integer imageCount = 3;

    @Column(name = "list_order", nullable = false)
    protected Integer listOrder = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @Column(name = "created_by")
    protected Long createdBy;

    @Column(name = "updated_by")
    protected Long updatedBy;
}
