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
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author : smk
 * @fileName : Categories
 * @date : 2025. 7. 16.
 */
@Entity
@Getter
@Table(name = "categories")
@Auditable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_no")
    private Integer categoryNo;

    @Column(name = "parent_category_no")
    private Integer parentCategoryNo;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "name_english", length = 100, nullable = false)
    private String nameEnglish;

    @Column(name = "code", length = 32, nullable = false, unique = true)
    private String code;

    @Column(name = "activated", nullable = false)
    private Boolean activated = true;

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

    // 자기 참조 관계 (부모 카테고리)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_no", insertable = false, updatable = false)
    @JsonIgnore
    private Categories parentCategory;

    // 자기 참조 관계 (자식 카테고리들)
    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Categories> childCategories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private Users creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", insertable = false, updatable = false)
    private Users updater;

    public static Categories createByBatch(String code, String name) {
        Categories category = new Categories();
        category.code = code;
        category.name = name;
        category.nameEnglish = name;
        category.createdBy = SystemConstants.SYSTEM_USER_NO;
        return category;
    }

    public void updateNameByBatch(String name) {
        this.name = name;
        this.updatedBy = SystemConstants.SYSTEM_USER_NO;
    }
}
