package com.gugus.batch.database.repositories;


import com.gugus.batch.database.entities.ColorChips;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : smk
 * @fileName : ColorChipsRepository
 * @date : 2025. 8. 9.
 */
public interface ColorChipsRepository extends JpaRepository<ColorChips, Integer> {
    List<ColorChips> findAllByOrderByListOrderAsc();
}
