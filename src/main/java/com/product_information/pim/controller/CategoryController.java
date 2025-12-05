package com.product_information.pim.controller;

import com.product_information.pim.dto.request.CategoryCreateRequest;
import com.product_information.pim.dto.request.CategoryUpdateRequest;
import com.product_information.pim.dto.response.ApiResponse;
import com.product_information.pim.dto.response.CategoryResponse;
import com.product_information.pim.dto.response.PageResponse;
import com.product_information.pim.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest request) {
        CategoryResponse response = categoryService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        CategoryResponse response = categoryService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Integer id) {
        CategoryResponse response = categoryService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponse> getBySlug(@PathVariable String slug) {
        CategoryResponse response = categoryService.getBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "false") boolean paginated,
            @PageableDefault(size = 20) Pageable pageable) {
        if (paginated) {
            Page<CategoryResponse> page = categoryService.getAll(pageable);
            return ResponseEntity.ok(new PageResponse<>(page));
        } else {
            List<CategoryResponse> list = categoryService.getAll();
            return ResponseEntity.ok(new ApiResponse<>(list));
        }
    }

    @GetMapping("/root")
    public ResponseEntity<ApiResponse<CategoryResponse>> getRootCategories() {
        List<CategoryResponse> list = categoryService.getRootCategories();
        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @GetMapping("/{id}/subcategories")
    public ResponseEntity<ApiResponse<CategoryResponse>> getSubCategories(@PathVariable Integer id) {
        List<CategoryResponse> list = categoryService.getSubCategories(id);
        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @GetMapping("/{id}/tree")
    public ResponseEntity<CategoryResponse> getCategoryTree(@PathVariable Integer id) {
        CategoryResponse response = categoryService.getCategoryTreeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<CategoryResponse>> getFullCategoryTree() {
        List<CategoryResponse> list = categoryService.getCategoryTree();
        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @PatchMapping("/{id}/reorder")
    public ResponseEntity<Void> reorder(
            @PathVariable Integer id,
            @RequestParam Integer order) {
        categoryService.reorder(id, order);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<Void> move(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer parentId) {
        categoryService.move(id, parentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
