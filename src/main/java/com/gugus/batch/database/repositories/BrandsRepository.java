package com.gugus.batch.database.repositories;

import com.gugus.batch.database.entities.Brands;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : smk
 * @fileName : BrandsRepository
 * @date : 2025-07-20
 */
public interface BrandsRepository extends JpaRepository<Brands, Integer> {
    List<Brands> findByBrandNoIn(List<Integer> brandNos);
    Optional<Brands> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByName(String name);
    int countByDeletedAtIsNull();
    boolean existsByCodeAndBrandNoNot(String code, Integer brandNo);
    

}
