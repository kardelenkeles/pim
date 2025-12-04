package com.product_information.pim.repository;

import com.product_information.pim.entity.Quality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QualityRepository extends JpaRepository<Quality, Integer> {

    Optional<Quality> findByProductId(Integer productId);

    @Query("SELECT q FROM Quality q WHERE q.score < :minScore")
    List<Quality> findLowQualityProducts(Integer minScore);

    @Query("SELECT AVG(q.score) FROM Quality q")
    Double getAverageQualityScore();
}
