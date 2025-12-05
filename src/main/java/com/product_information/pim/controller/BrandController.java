package com.product_information.pim.controller;

import com.product_information.pim.dto.request.BrandCreateRequest;
import com.product_information.pim.dto.request.BrandUpdateRequest;
import com.product_information.pim.dto.response.ApiResponse;
import com.product_information.pim.dto.response.BrandResponse;
import com.product_information.pim.dto.response.PageResponse;
import com.product_information.pim.service.BrandService;
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
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandResponse> create(@Valid @RequestBody BrandCreateRequest request) {
        BrandResponse response = brandService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody BrandUpdateRequest request) {
        BrandResponse response = brandService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getById(@PathVariable Integer id) {
        BrandResponse response = brandService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<BrandResponse> getBySlug(@PathVariable String slug) {
        BrandResponse response = brandService.getBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "false") boolean paginated,
            @PageableDefault(size = 20) Pageable pageable) {
        if (paginated) {
            Page<BrandResponse> page = brandService.getAll(pageable);
            return ResponseEntity.ok(new PageResponse<>(page));
        } else {
            List<BrandResponse> list = brandService.getAll();
            return ResponseEntity.ok(new ApiResponse<>(list));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<BrandResponse>> search(
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<BrandResponse> page = brandService.search(keyword, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
