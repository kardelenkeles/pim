package com.product_information.pim.controller;

import com.product_information.pim.dto.request.ProductAttributeRequest;
import com.product_information.pim.dto.request.ProductAttributeUpdateRequest;
import com.product_information.pim.dto.response.ProductAttributeResponse;
import com.product_information.pim.service.ProductAttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-attributes")
@RequiredArgsConstructor
public class ProductAttributeController {

    private final ProductAttributeService productAttributeService;

    @PostMapping
    public ResponseEntity<ProductAttributeResponse> create(@Valid @RequestBody ProductAttributeRequest request) {
        ProductAttributeResponse response = productAttributeService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductAttributeResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody ProductAttributeUpdateRequest request) {
        ProductAttributeResponse response = productAttributeService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductAttributeResponse> getById(@PathVariable Integer id) {
        ProductAttributeResponse response = productAttributeService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductAttributeResponse>> getByProductId(@PathVariable Integer productId) {
        List<ProductAttributeResponse> response = productAttributeService.getByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<List<ProductAttributeResponse>> getByKey(@PathVariable String key) {
        List<ProductAttributeResponse> response = productAttributeService.getByKey(key);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productAttributeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteByProductId(@PathVariable Integer productId) {
        productAttributeService.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}
