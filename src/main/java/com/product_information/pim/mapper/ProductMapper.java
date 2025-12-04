package com.product_information.pim.mapper;

import com.product_information.pim.dto.request.ProductCreateRequest;
import com.product_information.pim.dto.request.ProductUpdateRequest;
import com.product_information.pim.dto.response.ProductResponse;
import com.product_information.pim.entity.*;
import com.product_information.pim.enums.ProductStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    private final ProductImageMapper imageMapper;
    private final QualityMapper qualityMapper;

    public ProductMapper(ProductImageMapper imageMapper, QualityMapper qualityMapper) {
        this.imageMapper = imageMapper;
        this.qualityMapper = qualityMapper;
    }

    public Product toEntity(ProductCreateRequest request) {
        Product product = new Product();
        product.setBarcode(request.getBarcode());
        product.setCategoryId(request.getCategoryId());
        product.setBrandId(request.getBrandId());
        product.setTitle(request.getTitle());
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus() != null ? request.getStatus() : ProductStatus.DRAFT);
        return product;
    }

    public void updateEntity(Product product, ProductUpdateRequest request) {
        if (request.getBarcode() != null) {
            product.setBarcode(request.getBarcode());
        }
        if (request.getCategoryId() != null) {
            product.setCategoryId(request.getCategoryId());
        }
        if (request.getBrandId() != null) {
            product.setBrandId(request.getBrandId());
        }
        if (request.getTitle() != null) {
            product.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            product.setStatus(request.getStatus());
        }
    }

    public List<ProductAttribute> toAttributeEntities(Integer productId, Map<String, String> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return new ArrayList<>();
        }

        return attributes.entrySet().stream()
                .map(entry -> {
                    ProductAttribute attr = new ProductAttribute();
                    attr.setProductId(productId);
                    attr.setKey(entry.getKey());
                    attr.setValue(entry.getValue());
                    return attr;
                })
                .collect(Collectors.toList());
    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .barcode(product.getBarcode())
                .categoryId(product.getCategoryId())
                .brandId(product.getBrandId())
                .title(product.getTitle())
                .description(product.getDescription())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .attributes(new HashMap<>())
                .images(new ArrayList<>())
                .build();
    }

    public ProductResponse toFullResponse(
            Product product,
            Category category,
            Brand brand,
            List<ProductAttribute> attributes,
            List<ProductImage> images,
            Quality quality) {
        ProductResponse response = toResponse(product);

        if (category != null) {
            response.setCategoryName(category.getName());
        }

        if (brand != null) {
            response.setBrandName(brand.getName());
        }

        if (attributes != null && !attributes.isEmpty()) {
            Map<String, String> attrMap = attributes.stream()
                    .collect(Collectors.toMap(
                            ProductAttribute::getKey,
                            ProductAttribute::getValue));
            response.setAttributes(attrMap);
        }

        if (images != null && !images.isEmpty()) {
            response.setImages(
                    images.stream()
                            .map(imageMapper::toResponse)
                            .collect(Collectors.toList()));
        }

        if (quality != null) {
            response.setQuality(qualityMapper.toResponse(quality));
        }

        return response;
    }
}
