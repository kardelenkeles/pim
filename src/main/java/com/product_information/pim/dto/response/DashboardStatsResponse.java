package com.product_information.pim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private Long totalProducts;
    private Long totalBrands;
    private Long totalCategories;
    private Long draftProducts;
    private Long activeProducts;
    private Long archivedProducts;
    private Double averageQualityScore;
    private Long productsWithImages;
    private Long productsWithAttributes;
}
