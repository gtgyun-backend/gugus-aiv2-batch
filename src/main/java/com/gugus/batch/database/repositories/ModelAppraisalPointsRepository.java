package com.gugus.batch.database.repositories;

import com.gugus.batch.database.entities.ModelAppraisalPoints;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : gtg
 * @fileName : ModelAppraisalPointsRepository
 * @date : 2025-01-27
 */
public interface ModelAppraisalPointsRepository extends JpaRepository<ModelAppraisalPoints, Long> {

    List<ModelAppraisalPoints> findByModelNo(Long modelNo);
    
    Optional<ModelAppraisalPoints> findByModelNoAndPointNo(Long modelNo, Long pointNo);
    
    void deleteByModelNo(Long modelNo);
}
