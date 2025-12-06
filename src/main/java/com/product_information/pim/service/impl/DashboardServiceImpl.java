package com.product_information.pim.service.impl;

import com.product_information.pim.dto.response.DashboardStatsResponse;
import com.product_information.pim.entity.Product;
import com.product_information.pim.enums.ProductStatus;
import com.product_information.pim.repository.BrandRepository;
import com.product_information.pim.repository.CategoryRepository;
import com.product_information.pim.repository.ProductRepository;
import com.product_information.pim.repository.QualityRepository;
import com.product_information.pim.service.DashboardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final QualityRepository qualityRepository;
    private final ObjectMapper objectMapper;

    @Override
    public DashboardStatsResponse getDashboardStats() {
        log.info("Getting dashboard statistics");

        DashboardStatsResponse stats = new DashboardStatsResponse();

        stats.setTotalProducts(productRepository.count());
        stats.setTotalBrands(brandRepository.count());
        stats.setTotalCategories(categoryRepository.count());

        stats.setDraftProducts(productRepository.countByStatus(ProductStatus.DRAFT));
        stats.setActiveProducts(productRepository.countByStatus(ProductStatus.ACTIVE));
        stats.setArchivedProducts(productRepository.countByStatus(ProductStatus.ARCHIVED));

        Double avgScore = qualityRepository.findAll().stream()
                .mapToInt(q -> q.getScore())
                .average()
                .orElse(0.0);
        stats.setAverageQualityScore(Math.round(avgScore * 100.0) / 100.0);

        List<Product> allProducts = productRepository.findAll();
        stats.setProductsWithImages(allProducts.stream()
                .filter(p -> p.getProductImages() != null && !p.getProductImages().isEmpty())
                .count());

        stats.setProductsWithAttributes(allProducts.stream()
                .filter(p -> p.getProductAttributes() != null && !p.getProductAttributes().isEmpty())
                .count());

        log.info("Dashboard statistics retrieved successfully");
        return stats;
    }

    @Override
    public String exportProductsToCsv() {
        log.info("Exporting products to CSV");

        List<Product> products = productRepository.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Barcode,Title,Category ID,Brand ID,Status,Description,Created At,Updated At\n");

        for (Product product : products) {
            csv.append(String.format("%d,\"%s\",\"%s\",%s,%s,%s,\"%s\",%s,%s\n",
                    product.getId(),
                    escapeCsv(product.getBarcode()),
                    escapeCsv(product.getTitle()),
                    product.getCategoryId(),
                    product.getBrandId(),
                    product.getStatus(),
                    escapeCsv(product.getDescription()),
                    product.getCreatedAt(),
                    product.getUpdatedAt()));
        }

        log.info("Products exported to CSV successfully: {} products", products.size());
        return csv.toString();
    }

    @Override
    public String exportProductsToJson() {
        log.info("Exporting products to JSON");

        try {
            List<Product> products = productRepository.findAll();

            // Convert to simple map to avoid circular reference issues
            List<Map<String, Object>> simpleProducts = products.stream()
                    .map(p -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", p.getId());
                        map.put("barcode", p.getBarcode());
                        map.put("title", p.getTitle());
                        map.put("categoryId", p.getCategoryId());
                        map.put("brandId", p.getBrandId());
                        map.put("status", p.getStatus());
                        map.put("description", p.getDescription());
                        map.put("createdAt", p.getCreatedAt());
                        map.put("updatedAt", p.getUpdatedAt());

                        // Add counts for related entities
                        if (p.getProductAttributes() != null) {
                            map.put("attributeCount", p.getProductAttributes().size());
                        }
                        if (p.getProductImages() != null) {
                            map.put("imageCount", p.getProductImages().size());
                        }
                        if (p.getQuality() != null) {
                            map.put("qualityScore", p.getQuality().getScore());
                        }

                        return map;
                    })
                    .collect(Collectors.toList());

            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(simpleProducts);

            log.info("Products exported to JSON successfully: {} products", products.size());
            return json;
        } catch (Exception e) {
            log.error("Error exporting products to JSON", e);
            throw new RuntimeException("Failed to export products to JSON", e);
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\"\"");
    }
}
