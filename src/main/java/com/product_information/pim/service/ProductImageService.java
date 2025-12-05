package com.product_information.pim.service;

import com.product_information.pim.dto.request.ProductImageCreateRequest;
import com.product_information.pim.dto.request.ProductImageUpdateRequest;
import com.product_information.pim.dto.response.ProductImageResponse;

import java.util.List;
import java.util.Map;

public interface ProductImageService {
    ProductImageResponse addImage(ProductImageCreateRequest request);

    ProductImageResponse updateImage(Integer id, ProductImageUpdateRequest request);

    ProductImageResponse getById(Integer id);

    List<ProductImageResponse> getByProductId(Integer productId);

    void reorderImages(Integer productId, Map<Integer, Integer> imageOrders);

    void deleteImage(Integer id);
}
