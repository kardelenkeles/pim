package com.product_information.pim.service;

import com.product_information.pim.dto.request.ProductAttributeRequest;
import com.product_information.pim.dto.request.ProductAttributeUpdateRequest;
import com.product_information.pim.dto.response.ProductAttributeResponse;

import java.util.List;

public interface ProductAttributeService {

    ProductAttributeResponse create(ProductAttributeRequest request);

    ProductAttributeResponse update(Integer id, ProductAttributeUpdateRequest request);

    ProductAttributeResponse getById(Integer id);

    List<ProductAttributeResponse> getByProductId(Integer productId);

    List<ProductAttributeResponse> getByKey(String key);

    void delete(Integer id);

    void deleteByProductId(Integer productId);
}
