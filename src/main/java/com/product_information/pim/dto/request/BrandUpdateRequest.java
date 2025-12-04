package com.product_information.pim.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandUpdateRequest {

    @Size(max = 100, message = "Brand name must not exceed 100 characters")
    private String name;
}
