package com.gugus.batch.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gugus.batch.auditlog.service.Auditable;
import com.gugus.batch.dto.GoodsItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
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
    private Long legacyGoodsNo;

    @Column(name = "category_code", length = 32, nullable = false)
    private String categoryCode;

    @Column(name = "brand_code", length = 32, nullable = false)
    private String brandCode;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "name_english", length = 100, nullable = false)
    private String nameEnglish;

    @Column(name = "product_model_code", length = 32)
    private String productModelCode;

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

    @Column(name = "domain")
    private String domain;

    @Lob
    @Column(name = "goods_image_url", columnDefinition = "TEXT")
    private String goodsImageUrl;

    @Column(name = "serial_no", length = 100)
    private String serialNo;

    @Column(name = "brand_model_no", length = 100)
    private String brandModelNo;

    @JsonIgnore
    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY)
    private List<GoodsProperty> goodsProperties;

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

    public static Goods createByBatch(GoodsItem item, Integer materialNo, Integer colorNo, Integer originNo, Long userNo, LocalDateTime createdAt, LocalDateTime updatedAt) {
        var goods = new Goods();
        goods.legacyGoodsNo = item.goodsNo();
        goods.categoryCode = item.categoryCode();
        goods.brandCode = item.brandCode();
        goods.name = item.modelName();
        goods.nameEnglish = item.modelName();
        goods.productModelCode = item.modelCode();
        goods.materialNo = materialNo;
        goods.colorNo = colorNo;
        goods.originNo = originNo;
        String imageUrl = item.goodsImageUrl();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            try {
                java.net.URI uri = java.net.URI.create(imageUrl);
                goods.domain = uri.getScheme() + "://" + uri.getHost();
                goods.goodsImageUrl = uri.getPath();
            } catch (Exception ignored) {
            }
        }
        goods.createdAt = createdAt;
        goods.updatedAt = updatedAt;
        goods.createdBy = userNo;
        return goods;
    }

    public void updateNameByBatch(GoodsItem item, Integer materialNo, Integer colorNo, Integer originNo, Long userNo, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = item.modelName();
        this.nameEnglish = item.modelName();
        this.categoryCode = item.categoryCode();
        this.brandCode = item.brandCode();
        this.materialNo = materialNo;
        this.colorNo = colorNo;
        this.originNo = originNo;
        String imageUrl = item.goodsImageUrl();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            try {
                java.net.URI uri = java.net.URI.create(imageUrl);
                this.domain = uri.getScheme() + "://" + uri.getHost();
                this.goodsImageUrl = uri.getPath();
            } catch (Exception ignored) {
            }
        }
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.updatedBy = userNo;
    }
}
