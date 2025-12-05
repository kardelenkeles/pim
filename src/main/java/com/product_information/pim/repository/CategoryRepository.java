package com.product_information.pim.repository;

import com.product_information.pim.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findBySlug(String slug);

    @Query("SELECT c FROM Category c WHERE c.parentCategoryId = :parentId ORDER BY c.order ASC")
    List<Category> findByParentCategoryId(@Param("parentId") Integer parentCategoryId);

    List<Category> findByParentCategoryOrderByOrderAsc(Category parentCategory);

    @Query("SELECT c FROM Category c WHERE c.parentCategoryId IS NULL ORDER BY c.order ASC")
    List<Category> findRootCategories();

    boolean existsBySlug(String slug);
}
