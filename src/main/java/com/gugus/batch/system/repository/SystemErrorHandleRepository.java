package com.gugus.batch.system.repository;

import com.gugus.batch.database.repositories.SystemLanguageMasterRepository;
import com.gugus.batch.system.dto.SystemErrorDto;
import com.gugus.batch.system.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class SystemErrorHandleRepository {

    private final SystemLanguageMasterRepository systemLanguageMasterRepository;

    public SystemErrorDto errorResponse(BusinessException exception) {
        var languageOpt = systemLanguageMasterRepository.findByIdLanguageCodeAndIdLanguageType(
                exception.getErrorCode(), exception.getLanguage());
        languageOpt.ifPresent(
                systemLanguageMaster -> exception.setMessage(systemLanguageMaster.getContent()));
        return new SystemErrorDto(exception);

    }
}