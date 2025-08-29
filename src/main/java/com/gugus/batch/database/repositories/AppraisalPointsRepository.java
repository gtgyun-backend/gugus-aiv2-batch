package com.gugus.batch.database.repositories;

import com.gugus.batch.database.entities.AppraisalPoints;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : gtg
 * @fileName : AppraisalPointsRepository
 * @date : 2025-01-27
 */
public interface AppraisalPointsRepository extends JpaRepository<AppraisalPoints, Long> {

    Optional<AppraisalPoints> findByPointNo(Long pointNo);
    Optional<AppraisalPoints> findByName(String name);
}
