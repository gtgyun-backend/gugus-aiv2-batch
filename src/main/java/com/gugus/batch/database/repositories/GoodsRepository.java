package com.gugus.batch.database.repositories;

import com.gugus.batch.database.entities.Goods;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : smk
 * @fileName : GoodsRepository
 * @date : 2025-08-29
 */
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    Optional<Goods> findByLegacyGoodsNo(Long legacyGoodsNo);
}
