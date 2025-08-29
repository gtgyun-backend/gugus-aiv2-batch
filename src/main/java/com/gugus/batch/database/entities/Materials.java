package com.gugus.batch.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author : smk
 * @fileName : Materials
 * @date : 2025. 8. 7.
 */
@Entity
@Getter
@Table(name = "materials", indexes = {
        @Index(name = "materials_list_order_IDX", columnList = "list_order")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Materials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_no")
    private Integer materialNo;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "name_english", length = 100, nullable = false)
    private String nameEnglish;

    @Column(name = "list_order", nullable = false)
    private Integer listOrder = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;
}
