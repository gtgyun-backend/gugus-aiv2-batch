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
 * @fileName : ProductModels
 * @date : 2025. 8. 29.
 */
@Entity
@Getter
@Table(name = "product_models")
@Auditable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductModels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_model_no")
    private Long productModelNo;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "code", length = 32, nullable = false)
    private String code;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private Long createdBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private Users creator;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Long updatedBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", insertable = false, updatable = false)
    private Users updater;

    public static ProductModels createByBatch(String code, String name) {
        ProductModels productModel = new ProductModels();
        productModel.code = code;
        productModel.name = name;
        productModel.createdBy = SystemConstants.SYSTEM_USER_NO;
        return productModel;
    }

    public void updateNameByBatch(String name) {
        this.name = name;
        this.updatedBy = SystemConstants.SYSTEM_USER_NO;
    }
}
