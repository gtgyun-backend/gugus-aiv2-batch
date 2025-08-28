package com.gugus.batch.database.repositories;

import com.gugus.batch.database.entities.Models;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : smk
 * @fileName : ModelsRepository
 * @date : 2025-07-22
 */
public interface ModelsRepository extends JpaRepository<Models, Long> {

    List<Models> findByCategoryNoAndBrandNo(Integer categoryNo, Integer brandNo);
    Optional<Models> findByCategoryNoAndBrandNoAndModelNo(Integer categoryNo, Integer brandNo, Long modelNo);

    Optional<Models> findByModelNo(Long modelNo);

    Optional<Models> findByCode(String modelCode);

    Optional<Models> findByCodeAndBrandNoAndCategoryNo(String code, Integer brandNo, Integer categoryNo);
    

}
