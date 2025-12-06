package com.product_information.pim.repository;

import com.product_information.pim.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer> {

    List<ProductAttribute> findByProductId(Integer productId);

    Optional<ProductAttribute> findByProductIdAndKey(Integer productId, String key);

    List<ProductAttribute> findByKey(String key);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductAttribute pa WHERE pa.productId = :productId")
    int deleteByProductId(Integer productId);
}
