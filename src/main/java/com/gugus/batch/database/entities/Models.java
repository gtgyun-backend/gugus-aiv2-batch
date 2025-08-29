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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author : smk
 * @fileName : Models
 * @date : 2025. 7. 16.
 */
@Entity
@Getter
@Table(name = "models")
@Auditable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Models {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_no")
    private Long modelNo;

    @Column(name = "main_goods_no")
    private Long mainGoodsNo;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "name_english", length = 100, nullable = false)
    private String nameEnglish;

    @Column(name = "code", length = 32, nullable = false)
    private String code;

    @Column(name = "category_no", nullable = false)
    private Integer categoryNo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_no", insertable = false, updatable = false)
    private Categories category;

    @Column(name = "brand_no", nullable = false)
    private Integer brandNo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_no", insertable = false, updatable = false)
    private Brands brand;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "memo", length = 1000)
    private String memo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private Users creator;

    @Column(name = "updated_by")
    private Long updatedBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", insertable = false, updatable = false)
    private Users updater;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_goods_no", insertable = false, updatable = false)
    private Goods mainGoods;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "model")
    private List<Goods> goods = new ArrayList<>();

    public static Models createByBatch(String code, String name, Integer brandNo, Integer categoryNo) {
        Models model = new Models();
        model.code = code;
        model.name = name;
        model.nameEnglish = name;
        model.brandNo = brandNo;
        model.categoryNo = categoryNo;
        model.createdBy = SystemConstants.SYSTEM_USER_NO;
        return model;
    }

    public void updateNameByBatch(String name, Long userNo) {
        this.name = name;
        this.updatedBy = SystemConstants.SYSTEM_USER_NO;
    }
}
