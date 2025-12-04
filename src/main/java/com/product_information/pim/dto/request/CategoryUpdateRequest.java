package com.product_information.pim.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {

    private Integer parentCategoryId;

    @Size(max = 100, message = "Category name must not exceed 100 characters")
    private String name;

    private String description;

    private Integer order;
}
