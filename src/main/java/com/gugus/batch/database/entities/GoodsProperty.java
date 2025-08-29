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
 * @fileName : GoodsProperty
 * @date : 2025. 1. 27.
 */
@Entity
@Getter
@Table(name = "goods_property")
@Auditable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_no")
    private Long propertyNo;

    @Column(name = "goods_no", nullable = false)
    private Long goodsNo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_no", insertable = false, updatable = false)
    private Goods goods;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "value", length = 100, nullable = false)
    private String value;

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

    public static GoodsProperty createByBatch(Goods goods, GoodsItem.GoodsProperty property, Long userNo) {
        var newProperty = new GoodsProperty();
        newProperty.goodsNo = goods.getGoodsNo();
        newProperty.name = property.label();
        newProperty.value = property.value();
        newProperty.createdBy = userNo;
        return newProperty;
    }
}
