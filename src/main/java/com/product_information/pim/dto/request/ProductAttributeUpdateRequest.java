package com.product_information.pim.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeUpdateRequest {

    @Size(max = 100, message = "Key must not exceed 100 characters")
    private String key;

    private String value;
}
