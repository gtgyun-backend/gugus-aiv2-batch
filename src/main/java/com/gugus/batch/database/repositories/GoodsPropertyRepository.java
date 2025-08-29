package com.gugus.batch.database.repositories;

import com.gugus.batch.database.entities.GoodsProperty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : smk
 * @fileName : GoodsPropertyRepository
 * @date : 2025-08-29
 */
public interface GoodsPropertyRepository extends JpaRepository<GoodsProperty, Long> {
}
