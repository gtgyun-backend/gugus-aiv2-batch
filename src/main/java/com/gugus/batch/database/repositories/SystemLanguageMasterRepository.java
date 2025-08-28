package com.gugus.batch.database.repositories;

import com.gugus.batch.database.entities.SystemLanguageMaster;
import com.gugus.batch.database.entities.SystemLanguageMasterId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : smk
 * @fileName : UsersRepository
 * @date : 2025. 7. 13.
 */
public interface SystemLanguageMasterRepository extends JpaRepository<SystemLanguageMaster, SystemLanguageMasterId> {
    Optional<SystemLanguageMaster> findByIdLanguageCodeAndIdLanguageType(String languageCode, String languageType);
}
