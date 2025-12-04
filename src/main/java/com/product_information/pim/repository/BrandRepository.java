package com.product_information.pim.repository;

import com.product_information.pim.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    Optional<Brand> findBySlug(String slug);

    Optional<Brand> findByName(String name);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);
}
