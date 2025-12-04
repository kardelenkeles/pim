package com.product_information.pim.service;

import com.product_information.pim.dto.request.CategoryCreateRequest;
import com.product_information.pim.dto.request.CategoryUpdateRequest;
import com.product_information.pim.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryCreateRequest request);

    CategoryResponse update(Integer id, CategoryUpdateRequest request);

    CategoryResponse getById(Integer id);

    CategoryResponse getBySlug(String slug);

    List<CategoryResponse> getAll();

    Page<CategoryResponse> getAll(Pageable pageable);

    List<CategoryResponse> getRootCategories();

    List<CategoryResponse> getSubCategories(Integer parentId);

    CategoryResponse getCategoryWithSubCategories(Integer id);

    void delete(Integer id);
}
