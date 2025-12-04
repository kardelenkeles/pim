package com.product_information.pim.mapper;

import com.product_information.pim.dto.request.CategoryCreateRequest;
import com.product_information.pim.dto.request.CategoryUpdateRequest;
import com.product_information.pim.dto.response.CategoryResponse;
import com.product_information.pim.entity.Category;
import com.product_information.pim.util.SlugUtil;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryCreateRequest request) {
        Category category = new Category();
        category.setParentCategoryId(request.getParentCategoryId());
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSlug(SlugUtil.generateSlug(request.getName()));
        category.setOrder(request.getOrder());
        return category;
    }

    public void updateEntity(Category category, CategoryUpdateRequest request) {
        if (request.getName() != null) {
            category.setName(request.getName());
            category.setSlug(SlugUtil.generateSlug(request.getName()));
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getParentCategoryId() != null) {
            category.setParentCategoryId(request.getParentCategoryId());
        }
        if (request.getOrder() != null) {
            category.setOrder(request.getOrder());
        }
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .parentCategoryId(category.getParentCategoryId())
                .name(category.getName())
                .description(category.getDescription())
                .slug(category.getSlug())
                .order(category.getOrder())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .subCategories(Collections.emptyList())
                .build();
    }

    public CategoryResponse toResponse(Category category, Long productCount) {
        CategoryResponse response = toResponse(category);
        response.setProductCount(productCount);
        return response;
    }

    public CategoryResponse toResponseWithSubCategories(Category category, List<Category> subCategories) {
        CategoryResponse response = toResponse(category);
        response.setSubCategories(
                subCategories.stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList()));
        return response;
    }
}
