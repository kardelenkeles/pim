package com.product_information.pim.mapper;

import com.product_information.pim.dto.request.ProductAttributeRequest;
import com.product_information.pim.dto.request.ProductAttributeUpdateRequest;
import com.product_information.pim.dto.response.ProductAttributeResponse;
import com.product_information.pim.entity.ProductAttribute;
import org.springframework.stereotype.Component;

@Component
public class ProductAttributeMapper {

    public ProductAttribute toEntity(ProductAttributeRequest request) {
        ProductAttribute attribute = new ProductAttribute();
        attribute.setProductId(request.getProductId());
        attribute.setKey(request.getKey());
        attribute.setValue(request.getValue());
        return attribute;
    }

    public void updateEntity(ProductAttribute attribute, ProductAttributeUpdateRequest request) {
        if (request.getKey() != null) {
            attribute.setKey(request.getKey());
        }
        if (request.getValue() != null) {
            attribute.setValue(request.getValue());
        }
    }

    public ProductAttributeResponse toResponse(ProductAttribute attribute) {
        return ProductAttributeResponse.builder()
                .id(attribute.getId())
                .productId(attribute.getProductId())
                .key(attribute.getKey())
                .value(attribute.getValue())
                .build();
    }
}
