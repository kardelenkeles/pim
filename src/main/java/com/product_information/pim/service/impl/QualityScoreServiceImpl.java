package com.product_information.pim.service.impl;

import com.product_information.pim.entity.Product;
import com.product_information.pim.entity.Quality;
import com.product_information.pim.exception.ResourceNotFoundException;
import com.product_information.pim.repository.ProductRepository;
import com.product_information.pim.repository.QualityRepository;
import com.product_information.pim.service.QualityScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QualityScoreServiceImpl implements QualityScoreService {

    private final ProductRepository productRepository;
    private final QualityRepository qualityRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Integer calculateScore(Product product) {
        int totalFields = 0;
        int filledFields = 0;

        // Basic fields (required fields always count as filled if product exists)
        totalFields += 5; // barcode, categoryId, brandId, title, status
        filledFields += 5;

        // Description (optional but important)
        totalFields++;
        if (product.getDescription() != null && !product.getDescription().trim().isEmpty()) {
            filledFields++;
        }

        // Attributes (optional)
        totalFields++;
        if (product.getProductAttributes() != null && !product.getProductAttributes().isEmpty()) {
            filledFields++;
        }

        // Images (optional)
        totalFields++;
        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            filledFields++;
        }

        // Calculate percentage
        int score = (int) Math.round((double) filledFields / totalFields * 100);

        log.debug("Quality score calculated for product {}: {} ({}/{})",
                product.getId(), score, filledFields, totalFields);

        return score;
    }

    @Override
    @Transactional
    public void updateQualityScore(Integer productId) {
        log.info("Updating quality score for product id: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Integer score = calculateScore(product);

        // Create detailed result JSON
        Map<String, Object> result = new HashMap<>();
        result.put("hasDescription", product.getDescription() != null && !product.getDescription().trim().isEmpty());
        result.put("attributeCount",
                product.getProductAttributes() != null ? product.getProductAttributes().size() : 0);
        result.put("imageCount", product.getProductImages() != null ? product.getProductImages().size() : 0);
        result.put("completenessPercentage", score);

        try {
            String resultJson = objectMapper.writeValueAsString(result);

            Quality quality = qualityRepository.findByProductId(productId)
                    .orElse(new Quality());

            quality.setProductId(productId);
            quality.setScore(score);
            quality.setResult(resultJson);

            qualityRepository.save(quality);

            log.info("Quality score updated successfully for product {}: {}", productId, score);
        } catch (Exception e) {
            log.error("Error updating quality score for product {}", productId, e);
            throw new RuntimeException("Failed to update quality score", e);
        }
    }
}
