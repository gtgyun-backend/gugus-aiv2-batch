package com.gugus.batch.database.repositories;

import com.gugus.batch.database.entities.Models;
import com.gugus.batch.database.entities.ProductModels;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : smk
 * @fileName : ModelsRepository
 * @date : 2025-07-22
 */
public interface ProductModelsRepository extends JpaRepository<ProductModels, Long> {

}
