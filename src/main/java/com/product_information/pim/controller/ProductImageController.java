package com.product_information.pim.controller;

import com.product_information.pim.dto.request.ProductImageCreateRequest;
import com.product_information.pim.dto.request.ProductImageUpdateRequest;
import com.product_information.pim.dto.response.ApiResponse;
import com.product_information.pim.dto.response.ProductImageResponse;
import com.product_information.pim.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product-images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<ProductImageResponse> addImage(@Valid @RequestBody ProductImageCreateRequest request) {
        ProductImageResponse response = productImageService.addImage(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductImageResponse> updateImage(
            @PathVariable Integer id,
            @Valid @RequestBody ProductImageUpdateRequest request) {
        ProductImageResponse response = productImageService.updateImage(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageResponse> getById(@PathVariable Integer id) {
        ProductImageResponse response = productImageService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<ProductImageResponse>> getByProductId(@PathVariable Integer productId) {
        List<ProductImageResponse> list = productImageService.getByProductId(productId);
        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @PatchMapping("/product/{productId}/reorder")
    public ResponseEntity<Void> reorderImages(
            @PathVariable Integer productId,
            @RequestBody Map<Integer, Integer> imageOrders) {
        productImageService.reorderImages(productId, imageOrders);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer id) {
        productImageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
