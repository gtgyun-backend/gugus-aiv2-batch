package com.gugus.batch.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gugus.batch.auditlog.service.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author : smk
 * @fileName : Brands
 * @date : 2025. 7. 16.
 */
@Entity
@Getter
@Table(name = "brands")
@Auditable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brands {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_no")
    private Integer brandNo;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "name_english", length = 100, nullable = false)
    private String nameEnglish;

    @Column(name = "code", length = 32, nullable = false)
    private String code;

    @Column(name = "activated", nullable = false)
    private Boolean activated = true;

    @Column(name = "popular", nullable = false)
    private Boolean popular = true;

    @Column(name = "require_serial_no", nullable = false)
    private Boolean requireSerialNo = false;

    @Column(name = "require_brand_model_no", nullable = false)
    private Boolean requireBrandModelNo = false;

    @Column(name = "logo_domain")
    private String logoDomain;

    @Lob
    @Column(name = "logo", columnDefinition = "TEXT")
    private String logo;

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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private Users creator;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", insertable = false, updatable = false)
    private Users updater;

    private static final Long SYSTEM_USER_NO = 0L;

    public static Brands createByBatch(String code, String name) {
        Brands brand = new Brands();
        brand.code = code;
        brand.name = name;
        brand.nameEnglish = name;
        brand.createdBy = SYSTEM_USER_NO;
        return brand;
    }

    public void updateNameByBatch(String name, Long userNo) {
        this.name = name;
        this.updatedBy = SYSTEM_USER_NO;
    }
}
