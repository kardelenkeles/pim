package com.product_information.pim.service.impl;

import com.product_information.pim.dto.request.CategoryCreateRequest;
import com.product_information.pim.dto.request.CategoryUpdateRequest;
import com.product_information.pim.dto.response.CategoryResponse;
import com.product_information.pim.entity.Category;
import com.product_information.pim.exception.DuplicateResourceException;
import com.product_information.pim.exception.ResourceNotFoundException;
import com.product_information.pim.mapper.CategoryMapper;
import com.product_information.pim.repository.CategoryRepository;
import com.product_information.pim.repository.ProductRepository;
import com.product_information.pim.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CategoryCreateRequest request) {
        log.info("Creating category: {}", request.getName());

        if (categoryRepository.existsBySlug(categoryMapper.toEntity(request).getSlug())) {
            throw new DuplicateResourceException("Category", "slug", request.getName());
        }

        if (request.getParentCategoryId() != null) {
            categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getParentCategoryId()));
        }

        Category category = categoryMapper.toEntity(request);
        category = categoryRepository.save(category);

        log.info("Category created successfully with id: {}", category.getId());
        return categoryMapper.toResponse(category, 0L);
    }

    @Override
    public CategoryResponse update(Integer id, CategoryUpdateRequest request) {
        log.info("Updating category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        if (request.getParentCategoryId() != null) {
            if (request.getParentCategoryId().equals(id)) {
                throw new DuplicateResourceException("Category", "parentCategoryId", id);
            }
            categoryRepository.findById(request.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", request.getParentCategoryId()));
        }

        categoryMapper.updateEntity(category, request);
        category = categoryRepository.save(category);

        Long productCount = productRepository.countByCategoryId(id);

        log.info("Category updated successfully with id: {}", id);
        return categoryMapper.toResponse(category, productCount);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(Integer id) {
        log.info("Fetching category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        Long productCount = productRepository.countByCategoryId(id);
        return categoryMapper.toResponse(category, productCount);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getBySlug(String slug) {
        log.info("Fetching category with slug: {}", slug);

        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "slug", slug));

        Long productCount = productRepository.countByCategoryId(category.getId());
        return categoryMapper.toResponse(category, productCount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {
        log.info("Fetching all categories");

        return categoryRepository.findAll().stream()
                .map(category -> {
                    Long productCount = productRepository.countByCategoryId(category.getId());
                    return categoryMapper.toResponse(category, productCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAll(Pageable pageable) {
        log.info("Fetching all categories with pagination");

        return categoryRepository.findAll(pageable)
                .map(category -> {
                    Long productCount = productRepository.countByCategoryId(category.getId());
                    return categoryMapper.toResponse(category, productCount);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getRootCategories() {
        log.info("Fetching root categories");

        return categoryRepository.findRootCategories().stream()
                .map(category -> {
                    Long productCount = productRepository.countByCategoryId(category.getId());
                    return categoryMapper.toResponse(category, productCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getSubCategories(Integer parentId) {
        log.info("Fetching subcategories for parent id: {}", parentId);

        return categoryRepository.findByParentCategoryId(parentId).stream()
                .map(category -> {
                    Long productCount = productRepository.countByCategoryId(category.getId());
                    return categoryMapper.toResponse(category, productCount);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryWithSubCategories(Integer id) {
        log.info("Fetching category with subcategories for id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        List<Category> subCategories = categoryRepository.findByParentCategoryId(id);

        return categoryMapper.toResponseWithSubCategories(category, subCategories);
    }

    @Override
    public void delete(Integer id) {
        log.info("Deleting category with id: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        categoryRepository.delete(category);

        log.info("Category deleted successfully with id: {}", id);
    }
}
