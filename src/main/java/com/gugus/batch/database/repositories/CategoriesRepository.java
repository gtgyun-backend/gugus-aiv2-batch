package com.gugus.batch.database.repositories;

import com.gugus.batch.database.entities.Categories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author : gtg
 * @fileName : CategoryRepository
 * @date : 2025-07-18
 */
public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
    Optional<Categories> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByName(String name);
    Integer countByParentCategoryNo(Integer parentCategoryNo);
    Optional<Categories> findByCategoryNo(Integer categoryNo);
    boolean existsByCodeAndCategoryNoNot(String code, Integer categoryNo);
    @Query("select distinct c from Categories c left join fetch c.creator where c.deletedAt is null order by c.listOrder asc")
    List<Categories> findAllWithCreator();
    List<Categories> findAllByActivatedAndDeletedAtIsNullOrderByListOrderAsc(boolean activated);

    @Modifying
    @Query("update Categories c set c.listOrder = c.listOrder + 1")
    void forceUpdateListOrderByNewCategory();
}
