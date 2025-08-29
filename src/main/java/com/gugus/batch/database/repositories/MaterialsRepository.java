package com.gugus.batch.database.repositories;

import com.gugus.batch.database.entities.Materials;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : smk
 * @fileName : MaterialsRepository
 * @date : 2025. 8. 9.
 */
public interface MaterialsRepository extends JpaRepository<Materials, Integer> {
}
