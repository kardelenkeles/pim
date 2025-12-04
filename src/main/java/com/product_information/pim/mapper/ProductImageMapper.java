package com.product_information.pim.mapper;

import com.product_information.pim.dto.request.ProductImageRequest;
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

    public ProductImageResponse toResponse(ProductImage image) {
        return ProductImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .altText(image.getAltText())
                .order(image.getOrder())
                .build();
    }
}
