package com.product_information.pim.mapper;

import com.product_information.pim.dto.request.ProductImageRequest;
import com.product_information.pim.dto.request.ProductImageCreateRequest;
import com.product_information.pim.dto.response.ProductImageResponse;
import com.product_information.pim.entity.ProductImage;
import org.springframework.stereotype.Component;

@Component
public class ProductImageMapper {

    public ProductImage toEntity(Integer productId, ProductImageRequest request) {
        ProductImage image = new ProductImage();
        image.setProductId(productId);
        image.setImageUrl(request.getImageUrl());
        image.setAltText(request.getAltText());
        image.setOrder(request.getOrder());
        return image;
    }

    public ProductImage toEntity(Integer productId, ProductImageCreateRequest request) {
        ProductImage image = new ProductImage();
        image.setProductId(productId);
        image.setImageUrl(request.getImageUrl());
        image.setAltText(request.getAltText());
        image.setOrder(request.getOrder());
        return image;
    }

    public ProductImageResponse toResponse(ProductImage image) {
        ProductImageResponse response = new ProductImageResponse();
        response.setId(image.getId());
        response.setProductId(image.getProductId());
        response.setImageUrl(image.getImageUrl());
        response.setAltText(image.getAltText());
        response.setOrder(image.getOrder());
        return response;
    }
}
