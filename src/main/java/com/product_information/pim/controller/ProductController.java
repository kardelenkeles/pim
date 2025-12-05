package com.product_information.pim.controller;

import com.product_information.pim.dto.request.ProductCreateRequest;
import com.product_information.pim.dto.request.ProductUpdateRequest;
import com.product_information.pim.dto.response.ApiResponse;
import com.product_information.pim.dto.response.PageResponse;
import com.product_information.pim.dto.response.ProductResponse;
import com.product_information.pim.enums.ProductStatus;
import com.product_information.pim.service.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest request) {
        ProductResponse response = productService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody ProductUpdateRequest request) {
        ProductResponse response = productService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Integer id) {
        ProductResponse response = productService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductResponse> getByBarcode(@PathVariable String barcode) {
        ProductResponse response = productService.getByBarcode(barcode);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "false") boolean paginated,
            @PageableDefault(size = 20) Pageable pageable) {
        if (paginated) {
            Page<ProductResponse> page = productService.getAll(pageable);
            return ResponseEntity.ok(new PageResponse<>(page));
        } else {
            List<ProductResponse> list = productService.getAll();
            return ResponseEntity.ok(new ApiResponse<>(list));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> search(
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ProductResponse> page = productService.search(keyword, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<PageResponse<ProductResponse>> searchWithFilters(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer brandId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ProductResponse> page = productService.searchWithFilters(
                keyword, status, categoryId, brandId, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<PageResponse<ProductResponse>> getByStatus(
            @PathVariable ProductStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ProductResponse> page = productService.getByStatus(status, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getByCategory(@PathVariable Integer categoryId) {
        List<ProductResponse> list = productService.getByCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getByBrand(@PathVariable Integer brandId) {
        List<ProductResponse> list = productService.getByBrand(brandId);
        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Integer id,
            @RequestParam ProductStatus status) {
        productService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
