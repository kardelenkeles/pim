package com.product_information.pim.service;

import com.product_information.pim.dto.request.ProductCreateRequest;
import com.product_information.pim.dto.request.ProductUpdateRequest;
import com.product_information.pim.dto.response.ProductResponse;
import com.product_information.pim.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductCreateRequest request);

    ProductResponse update(Integer id, ProductUpdateRequest request);

    ProductResponse getById(Integer id);

    ProductResponse getByBarcode(String barcode);

    List<ProductResponse> getAll();

    Page<ProductResponse> getAll(Pageable pageable);

    Page<ProductResponse> getByStatus(ProductStatus status, Pageable pageable);

    Page<ProductResponse> search(String keyword, Pageable pageable);

    Page<ProductResponse> searchWithFilters(
            String keyword,
            ProductStatus status,
            Integer categoryId,
            Integer brandId,
            Pageable pageable);

    List<ProductResponse> getByCategory(Integer categoryId);

    List<ProductResponse> getByBrand(Integer brandId);

    void delete(Integer id);

    void updateStatus(Integer id, ProductStatus status);
}
