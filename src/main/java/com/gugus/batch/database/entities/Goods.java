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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author : smk
 * @fileName : Goods
 * @date : 2025. 1. 27.
 */
@Entity
@Getter
@Table(name = "goods")
@Auditable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_no")
    private Long goodsNo;

    @Column(name = "appraisal_history_no")
    private Long appraisalHistoryNo;

    @Column(name = "legacy_goods_no")
    private Integer legacyGoodsNo;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "name_english", length = 100, nullable = false)
    private String nameEnglish;

    @Column(name = "model_no")
    private Long modelNo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_no", insertable = false, updatable = false)
    private Models model;

    @Column(name = "material_no")
    private Integer materialNo;

    @Column(name = "color_no")
    private Integer colorNo;

    @Column(name = "origin_no")
    private Integer originNo;

    @Lob
    @Column(name = "goods_image_url", columnDefinition = "TEXT")
    private String goodsImageUrl;

    @Column(name = "serial_no")
    private String serialNo;

    @Column(name = "brand_model_no")
    private String brandModelNo;

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
