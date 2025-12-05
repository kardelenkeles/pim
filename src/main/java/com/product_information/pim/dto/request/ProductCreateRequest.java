package com.product_information.pim.dto.request;

import com.product_information.pim.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "Barcode is required")
    @Size(max = 50, message = "Barcode must not exceed 50 characters")
    private String barcode;

    private Integer categoryId;

    private Integer brandId;

    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    private String description;

    private ProductStatus status = ProductStatus.DRAFT;

    private Map<String, String> attributes;

    private List<ProductImageRequest> images;
}
