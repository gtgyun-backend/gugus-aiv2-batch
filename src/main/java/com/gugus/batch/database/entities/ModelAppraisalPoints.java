package com.gugus.batch.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gugus.batch.auditlog.service.Auditable;
import com.gugus.batch.constants.SystemConstants;
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
 * @fileName : CategoriesBrandsAppraisalPointsModels
 * @date : 2025. 7. 16.
 */
@Entity
@Getter
@Table(name = "model_appraisal_points")
@Auditable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModelAppraisalPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_point_no")
    private Long modelPointNo;

    @Column(name = "point_no", nullable = false)
    private Long pointNo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_no", insertable = false, updatable = false)
    private AppraisalPoints appraisalPoint;

    @Column(name = "model_no", nullable = false)
    private Long modelNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_no", insertable = false, updatable = false)
    @JsonIgnore
    private Models model;

    @Column(name = "brand_point_no")
    private Long brandPointNo;

    @Column(name = "required", nullable = false)
    protected Boolean required = true;

    @Column(name = "image_count", nullable = false)
    protected Integer imageCount = 3;

    @Column(name = "list_order", nullable = false)
    protected Integer listOrder = 0;

    @Lob
    @Column(name = "guide_image_url", columnDefinition = "TEXT")
    private String guideImageUrl;

    @Column(name = "usable", nullable = false)
    private Boolean usable = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    public static ModelAppraisalPoints createByBatch(Long modelNo, Long pointNo, Integer imageCount, 
                                                   String guideImageUrl, String mandatoryYn, Integer listOrder) {
        ModelAppraisalPoints modelAppraisalPoints = new ModelAppraisalPoints();
        modelAppraisalPoints.modelNo = modelNo;
        modelAppraisalPoints.pointNo = pointNo;
        modelAppraisalPoints.imageCount = imageCount;
        modelAppraisalPoints.guideImageUrl = guideImageUrl;
        modelAppraisalPoints.required = "Y".equals(mandatoryYn);
        modelAppraisalPoints.listOrder = listOrder != null ? listOrder : 0;
        modelAppraisalPoints.usable = true;
        modelAppraisalPoints.createdBy = SystemConstants.SYSTEM_USER_NO;
        return modelAppraisalPoints;
    }

    public void updateByBatch(Integer imageCount, String guideImageUrl, String mandatoryYn, Integer listOrder) {
        this.imageCount = imageCount;
        this.guideImageUrl = guideImageUrl;
        this.required = "Y".equals(mandatoryYn);
        this.listOrder = listOrder != null ? listOrder : this.listOrder;
        this.updatedBy = SystemConstants.SYSTEM_USER_NO;
    }
}