package com.product_information.pim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Integer id;
    private Integer parentCategoryId;
    private String name;
    private String description;
    private String slug;
    private Integer order;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long productCount;
    private List<CategoryResponse> subCategories;
}
