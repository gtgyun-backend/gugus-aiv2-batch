package com.gugus.batch.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : smk
 * @fileName : ModelPutRequestDto
 * @date : 2025. 8. 16.
 */
@Getter
@Setter
public class ModelPutRequestDto {
    private Integer categoryNo;
    private Integer brandNo;
    private String name;
    private String nameEnglish;
    private String code;
    private Boolean isActivate;
    private String memo;
}
