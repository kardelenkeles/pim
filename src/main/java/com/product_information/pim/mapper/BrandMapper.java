package com.product_information.pim.mapper;

import com.product_information.pim.dto.request.BrandCreateRequest;
import com.product_information.pim.dto.request.BrandUpdateRequest;
import com.product_information.pim.dto.response.BrandResponse;
import com.product_information.pim.entity.Brand;
import com.product_information.pim.util.SlugUtil;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public Brand toEntity(BrandCreateRequest request) {
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setSlug(SlugUtil.generateSlug(request.getName()));
        return brand;
    }

    public void updateEntity(Brand brand, BrandUpdateRequest request) {
        if (request.getName() != null) {
            brand.setName(request.getName());
            brand.setSlug(SlugUtil.generateSlug(request.getName()));
        }
    }

    public BrandResponse toResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }

    public BrandResponse toResponse(Brand brand, Long productCount) {
        BrandResponse response = toResponse(brand);
        response.setProductCount(productCount);
        return response;
    }
}
