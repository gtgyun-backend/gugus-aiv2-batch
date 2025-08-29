package com.gugus.batch.database.entities;

import com.gugus.batch.auditlog.service.Auditable;
import com.gugus.batch.constants.SystemConstants;
import com.gugus.batch.database.entities.base.AppraisalPointRelationBase;

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

    public static AppraisalPoints createByBatch(String pointName, String nameEnglish) {
        var appraisalPoint = new AppraisalPoints();
        appraisalPoint.name = pointName;
        appraisalPoint.nameEnglish = nameEnglish != null ? nameEnglish : pointName;
        appraisalPoint.createdBy = SystemConstants.SYSTEM_USER_NO;
        return appraisalPoint;
    }

    public void updateByBatch(String pointName, String nameEnglish) {
        // 이름이 실제로 변경된 경우에만 업데이트
        if (!name.equals(pointName)) {
            this.name = pointName;
            this.updatedBy = SystemConstants.SYSTEM_USER_NO;
        }
    }
}
