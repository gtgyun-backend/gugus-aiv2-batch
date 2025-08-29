package com.gugus.batch.database.repositories;


import com.gugus.batch.database.entities.OriginCountries;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : smk
 * @fileName : OriginCountriesRepository
 * @date : 2025. 8. 9.
 */
public interface OriginCountriesRepository extends JpaRepository<OriginCountries, Integer> {

    List<OriginCountries> findAllByOrderByListOrderAsc();
}
