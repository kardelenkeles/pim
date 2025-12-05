package com.product_information.pim.repository;

import com.product_information.pim.entity.Product;
import com.product_information.pim.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

        Optional<Product> findByBarcode(String barcode);

        @Query("SELECT p FROM Product p WHERE " +
                        "(:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(p.barcode) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
                        "(:status IS NULL OR p.status = :status) AND " +
                        "(:categoryId IS NULL OR p.categoryId = :categoryId) AND " +
                        "(:brandId IS NULL OR p.brandId = :brandId)")
        Page<Product> searchProductsWithFilters(
                        @Param("keyword") String keyword,
                        @Param("status") ProductStatus status,
                        @Param("categoryId") Integer categoryId,
                        @Param("brandId") Integer brandId,
                        Pageable pageable);

        boolean existsByBarcode(String barcode);

        long countByStatus(ProductStatus status);

        long countByCategoryId(Integer categoryId);

        long countByBrandId(Integer brandId);
}
