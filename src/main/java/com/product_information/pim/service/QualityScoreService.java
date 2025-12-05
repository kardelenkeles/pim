package com.product_information.pim.service;

import com.product_information.pim.entity.Product;

public interface QualityScoreService {

    /**
     * Calculate quality score based on product data completeness
     * 
     * @param product Product entity
     * @return Quality score percentage (0-100)
     */
    Integer calculateScore(Product product);

    /**
     * Update quality score for a product
     * 
     * @param productId Product ID
     */
    void updateQualityScore(Integer productId);
}
