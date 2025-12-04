package com.product_information.pim.dto.response;

import com.product_information.pim.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Integer id;
    private String barcode;
    private Integer categoryId;
    private String categoryName;
    private Integer brandId;
    private String brandName;
    private String title;
    private String description;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Map<String, String> attributes;
    private List<ProductImageResponse> images;
    private QualityResponse quality;
}
