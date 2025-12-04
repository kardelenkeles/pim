package com.product_information.pim.repository;

import com.product_information.pim.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    List<ProductImage> findByProductIdOrderByOrderAsc(Integer productId);

    void deleteByProductId(Integer productId);

    long countByProductId(Integer productId);
}
