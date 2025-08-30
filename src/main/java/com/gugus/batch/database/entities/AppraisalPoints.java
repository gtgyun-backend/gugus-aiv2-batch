package com.gugus.batch.database.entities;

import com.gugus.batch.auditlog.service.Auditable;
import com.gugus.batch.constants.SystemConstants;
import com.gugus.batch.database.entities.base.AppraisalPointRelationBase;

import com.gugus.batch.dto.AppraisalPointItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : smk
 * @fileName : AppraisalPoints
 * @date : 2025. 8. 6.
 */
@Entity
@Getter
@Table(name = "appraisal_points")
@Auditable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppraisalPoints extends AppraisalPointRelationBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_no")
    private Long pointNo;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "name_english", length = 100, nullable = false)
    private String nameEnglish;

    public static AppraisalPoints createByBatch(AppraisalPointItem item) {
        var appraisalPoint = new AppraisalPoints();
        appraisalPoint.name = item.pointName();
        appraisalPoint.nameEnglish = item.pointName();
        appraisalPoint.imageCount = item.imageCount();
        appraisalPoint.required = "Y".equals(item.mandatoryYn());
        appraisalPoint.createdBy = SystemConstants.SYSTEM_USER_NO;
        return appraisalPoint;
    }

    public void updateByBatch(AppraisalPointItem item) {
        boolean isUpdated = false;
        if(!this.imageCount.equals(item.imageCount())) {
            this.imageCount = item.imageCount();
            isUpdated = true;
        }
        if(!this.required.equals("Y".equals(item.mandatoryYn()))) {
            this.required = "Y".equals(item.mandatoryYn());
            isUpdated = true;
        }
        if(isUpdated) {
            this.updatedBy = SystemConstants.SYSTEM_USER_NO;
        }
    }
}
