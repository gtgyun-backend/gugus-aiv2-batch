package com.gugus.batch.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : smk
 * @fileName : SystemLanguageMasterId
 * @date : 2025. 7. 19.
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class SystemLanguageMasterId implements Serializable {

    @Column(name = "language_code", length = 11)
    private String languageCode;

    @Column(name = "language_type", length = 7)
    private String languageType;
} 