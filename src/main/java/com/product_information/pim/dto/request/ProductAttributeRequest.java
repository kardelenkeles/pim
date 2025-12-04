package com.product_information.pim.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeRequest {

    @NotNull(message = "Product ID is required")
    private Integer productId;

    @NotBlank(message = "Attribute key is required")
    @Size(max = 100, message = "Key must not exceed 100 characters")
    private String key;

    @NotBlank(message = "Attribute value is required")
    private String value;
}
